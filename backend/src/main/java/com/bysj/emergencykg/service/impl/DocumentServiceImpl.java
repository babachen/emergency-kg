package com.bysj.emergencykg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.context.UserContext;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.mapper.ExtractionTaskMapper;
import com.bysj.emergencykg.mapper.PlanDocumentMapper;
import com.bysj.emergencykg.mapper.PlanSectionMapper;
import com.bysj.emergencykg.mapper.RegionMapper;
import com.bysj.emergencykg.model.dto.DocumentDTO;
import com.bysj.emergencykg.model.entity.ExtractionTask;
import com.bysj.emergencykg.model.entity.PlanDocument;
import com.bysj.emergencykg.model.entity.PlanSection;
import com.bysj.emergencykg.model.entity.Region;
import com.bysj.emergencykg.model.vo.DocumentVO;
import com.bysj.emergencykg.service.DocumentService;
import com.bysj.emergencykg.service.OperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final Pattern HEADING_PATTERN = Pattern.compile("^(第[一二三四五六七八九十0-9]+[章节].*|[一二三四五六七八九十]+、.*|（[一二三四五六七八九十]+）.*)$");
    private final RegionMapper regionMapper;
    private final PlanDocumentMapper planDocumentMapper;
    private final PlanSectionMapper planSectionMapper;
    private final ExtractionTaskMapper extractionTaskMapper;
    private final OperationLogService operationLogService;

    public DocumentServiceImpl(RegionMapper regionMapper, PlanDocumentMapper planDocumentMapper, PlanSectionMapper planSectionMapper, ExtractionTaskMapper extractionTaskMapper, OperationLogService operationLogService) {
        this.regionMapper = regionMapper;
        this.planDocumentMapper = planDocumentMapper;
        this.planSectionMapper = planSectionMapper;
        this.extractionTaskMapper = extractionTaskMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public List<Region> listRegions() {
        return regionMapper.selectList(new LambdaQueryWrapper<Region>().eq(Region::getStatus, 1).orderByAsc(Region::getSortOrder));
    }

    @Override
    public PageResult<DocumentVO.DocumentItemVO> pageDocuments(DocumentDTO.DocumentQueryDTO queryDTO) {
        LambdaQueryWrapper<PlanDocument> wrapper = new LambdaQueryWrapper<PlanDocument>()
                .eq(queryDTO.getRegionId() != null, PlanDocument::getRegionId, queryDTO.getRegionId())
                .eq(queryDTO.getPreprocessStatus() != null, PlanDocument::getPreprocessStatus, queryDTO.getPreprocessStatus())
                .eq(queryDTO.getExtractionStatus() != null, PlanDocument::getExtractionStatus, queryDTO.getExtractionStatus())
                .like(StringUtils.hasText(queryDTO.getKeyword()), PlanDocument::getTitle, queryDTO.getKeyword())
                .orderByDesc(PlanDocument::getCreateTime);
        Page<PlanDocument> page = planDocumentMapper.selectPage(new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize()), wrapper);
        Map<Long, String> regionNameMap = listRegions().stream().collect(Collectors.toMap(Region::getId, Region::getRegionName));
        return PageResult.of(page, doc -> toDocumentVO(doc, regionNameMap));
    }

    @Override
    public DocumentVO.DocumentItemVO getDocument(Long id) {
        PlanDocument doc = planDocumentMapper.selectById(id);
        if (doc == null) {
            throw new BusinessException("预案不存在");
        }
        Map<Long, String> regionNameMap = listRegions().stream().collect(Collectors.toMap(Region::getId, Region::getRegionName));
        return toDocumentVO(doc, regionNameMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDocument(DocumentDTO.DocumentSaveDTO dto) {
        PlanDocument doc = new PlanDocument();
        fillDoc(dto, doc);
        doc.setPreprocessStatus(0);
        doc.setExtractionStatus(0);
        doc.setCreateBy(UserContext.getUserId());
        doc.setCreateTime(LocalDateTime.now());
        doc.setUpdateTime(LocalDateTime.now());
        planDocumentMapper.insert(doc);
        operationLogService.log("预案管理", "新增", "新增预案：" + dto.getTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocument(Long id, DocumentDTO.DocumentSaveDTO dto) {
        PlanDocument doc = planDocumentMapper.selectById(id);
        if (doc == null) {
            throw new BusinessException("预案不存在");
        }
        fillDoc(dto, doc);
        doc.setUpdateTime(LocalDateTime.now());
        planDocumentMapper.updateById(doc);
        operationLogService.log("预案管理", "修改", "修改预案：" + doc.getTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(Long id) {
        PlanDocument doc = planDocumentMapper.selectById(id);
        if (doc == null) {
            return;
        }
        planSectionMapper.delete(new LambdaQueryWrapper<PlanSection>().eq(PlanSection::getDocumentId, id));
        extractionTaskMapper.delete(new LambdaQueryWrapper<ExtractionTask>().eq(ExtractionTask::getDocumentId, id));
        planDocumentMapper.deleteById(id);
        operationLogService.log("预案管理", "删除", "删除预案：" + doc.getTitle());
    }

    @Override
    public List<DocumentVO.SectionVO> listSections(Long documentId) {
        PlanDocument doc = planDocumentMapper.selectById(documentId);
        String title = doc == null ? "" : doc.getTitle();
        return planSectionMapper.selectList(new LambdaQueryWrapper<PlanSection>().eq(documentId != null, PlanSection::getDocumentId, documentId).orderByAsc(PlanSection::getSectionNo))
                .stream().map(section -> {
                    DocumentVO.SectionVO vo = new DocumentVO.SectionVO();
                    vo.setId(section.getId());
                    vo.setDocumentId(section.getDocumentId());
                    vo.setDocumentTitle(title);
                    vo.setSectionNo(section.getSectionNo());
                    vo.setSectionTitle(section.getSectionTitle());
                    vo.setSectionLevel(section.getSectionLevel());
                    vo.setSectionContent(section.getSectionContent());
                    vo.setWordCount(section.getWordCount());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int preprocess(Long documentId) {
        PlanDocument doc = planDocumentMapper.selectById(documentId);
        if (doc == null) {
            throw new BusinessException("预案不存在");
        }
        String content = loadContent(doc);
        if (!StringUtils.hasText(content)) {
            throw new BusinessException("预案正文为空，请先补录内容或上传 txt 文件");
        }
        List<SectionPiece> pieces = splitSections(content);
        planSectionMapper.delete(new LambdaQueryWrapper<PlanSection>().eq(PlanSection::getDocumentId, documentId));
        int index = 1;
        for (SectionPiece piece : pieces) {
            PlanSection section = new PlanSection();
            section.setDocumentId(documentId);
            section.setSectionNo(String.format("S%03d", index++));
            section.setSectionTitle(piece.title());
            section.setSectionLevel(piece.level());
            section.setSectionContent(piece.content());
            section.setWordCount(piece.content().length());
            section.setCreateTime(LocalDateTime.now());
            section.setUpdateTime(LocalDateTime.now());
            planSectionMapper.insert(section);
        }
        doc.setPreprocessStatus(1);
        if (doc.getExtractionStatus() == null || doc.getExtractionStatus() == 0) {
            doc.setExtractionStatus(1);
        }
        doc.setUpdateTime(LocalDateTime.now());
        planDocumentMapper.updateById(doc);
        operationLogService.log("预案管理", "预处理", "完成预案分段：" + doc.getTitle() + "，共 " + pieces.size() + " 段");
        return pieces.size();
    }

    private void fillDoc(DocumentDTO.DocumentSaveDTO dto, PlanDocument doc) {
        doc.setTitle(dto.getTitle());
        doc.setRegionId(dto.getRegionId());
        doc.setPlanYear(dto.getPlanYear());
        doc.setPlanType(dto.getPlanType());
        doc.setSourceUrl(dto.getSourceUrl());
        doc.setFileId(dto.getFileId());
        doc.setFileName(dto.getFileName());
        doc.setFilePath(dto.getFilePath());
        doc.setContent(dto.getContent());
        doc.setPublishOrg(dto.getPublishOrg());
        doc.setApprovalDate(dto.getApprovalDate());
        doc.setVersionNo(dto.getVersionNo());
        doc.setSummary(dto.getSummary());
    }

    private DocumentVO.DocumentItemVO toDocumentVO(PlanDocument doc, Map<Long, String> regionNameMap) {
        DocumentVO.DocumentItemVO vo = new DocumentVO.DocumentItemVO();
        vo.setId(doc.getId());
        vo.setTitle(doc.getTitle());
        vo.setRegionId(doc.getRegionId());
        vo.setRegionName(regionNameMap.get(doc.getRegionId()));
        vo.setPlanYear(doc.getPlanYear());
        vo.setPlanType(doc.getPlanType());
        vo.setSourceUrl(doc.getSourceUrl());
        vo.setFileId(doc.getFileId());
        vo.setFileName(doc.getFileName());
        vo.setFilePath(doc.getFilePath());
        vo.setPreprocessStatus(doc.getPreprocessStatus());
        vo.setPreprocessStatusText(statusText(doc.getPreprocessStatus(), Map.of(0, "未处理", 1, "已分段")));
        vo.setExtractionStatus(doc.getExtractionStatus());
        vo.setExtractionStatusText(statusText(doc.getExtractionStatus(), Map.of(0, "未抽取", 1, "待抽取", 2, "抽取中", 3, "已完成", 4, "失败")));
        vo.setPublishOrg(doc.getPublishOrg());
        vo.setApprovalDate(doc.getApprovalDate());
        vo.setVersionNo(doc.getVersionNo());
        vo.setSummary(doc.getSummary());
        vo.setContent(doc.getContent());
        vo.setCreateTime(doc.getCreateTime());
        vo.setSectionCount(Math.toIntExact(planSectionMapper.selectCount(new LambdaQueryWrapper<PlanSection>().eq(PlanSection::getDocumentId, doc.getId()))));
        return vo;
    }

    private String statusText(Integer code, Map<Integer, String> mapping) {
        return mapping.getOrDefault(code, "未知");
    }

    private String loadContent(PlanDocument doc) {
        if (StringUtils.hasText(doc.getContent())) {
            return doc.getContent().replace("\r", "");
        }
        if (StringUtils.hasText(doc.getFilePath())) {
            try {
                Path path = Path.of(doc.getFilePath());
                if (Files.exists(path) && path.toString().endsWith(".txt")) {
                    return Files.readString(path);
                }
            } catch (Exception ignored) {
            }
        }
        return "";
    }

    private List<SectionPiece> splitSections(String content) {
        List<SectionPiece> pieces = new ArrayList<>();
        String[] lines = content.replace("\r", "").split("\n");
        String currentTitle = "导言";
        StringBuilder builder = new StringBuilder();
        int currentLevel = 1;
        for (String raw : lines) {
            String line = raw.trim();
            if (!StringUtils.hasText(line)) {
                continue;
            }
            if (HEADING_PATTERN.matcher(line).matches()) {
                if (builder.length() > 0) {
                    pieces.add(new SectionPiece(currentTitle, builder.toString().trim(), currentLevel));
                    builder = new StringBuilder();
                }
                currentTitle = line;
                currentLevel = line.startsWith("第") ? 1 : 2;
            } else {
                builder.append(line).append("\n");
            }
        }
        if (builder.length() > 0) {
            pieces.add(new SectionPiece(currentTitle, builder.toString().trim(), currentLevel));
        }
        if (pieces.isEmpty()) {
            String text = content.trim();
            int size = 350;
            for (int i = 0; i < text.length(); i += size) {
                int end = Math.min(text.length(), i + size);
                pieces.add(new SectionPiece("正文片段" + (pieces.size() + 1), text.substring(i, end), 2));
            }
        }
        return pieces;
    }

    private record SectionPiece(String title, String content, int level) {}
}
