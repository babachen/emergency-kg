package com.bysj.emergencykg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.context.UserContext;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.mapper.*;
import com.bysj.emergencykg.model.dto.DocumentDTO;
import com.bysj.emergencykg.model.entity.*;
import com.bysj.emergencykg.model.vo.DocumentVO;
import com.bysj.emergencykg.service.DocumentService;
import com.bysj.emergencykg.service.ExtractionService;
import com.bysj.emergencykg.service.OperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExtractionServiceImpl implements ExtractionService {
    private final ExtractionTaskMapper extractionTaskMapper;
    private final PlanDocumentMapper planDocumentMapper;
    private final PlanSectionMapper planSectionMapper;
    private final RegionMapper regionMapper;
    private final EntityTypeMapper entityTypeMapper;
    private final RelationTypeMapper relationTypeMapper;
    private final KgEntityMapper kgEntityMapper;
    private final KgRelationMapper kgRelationMapper;
    private final KgTripleMapper kgTripleMapper;
    private final KnowledgeConflictMapper knowledgeConflictMapper;
    private final KnowledgeCompletionMapper knowledgeCompletionMapper;
    private final DocumentService documentService;
    private final AiSupportService aiSupportService;
    private final OperationLogService operationLogService;

    public ExtractionServiceImpl(ExtractionTaskMapper extractionTaskMapper, PlanDocumentMapper planDocumentMapper, PlanSectionMapper planSectionMapper, RegionMapper regionMapper, EntityTypeMapper entityTypeMapper, RelationTypeMapper relationTypeMapper, KgEntityMapper kgEntityMapper, KgRelationMapper kgRelationMapper, KgTripleMapper kgTripleMapper, KnowledgeConflictMapper knowledgeConflictMapper, KnowledgeCompletionMapper knowledgeCompletionMapper, DocumentService documentService, AiSupportService aiSupportService, OperationLogService operationLogService) {
        this.extractionTaskMapper = extractionTaskMapper;
        this.planDocumentMapper = planDocumentMapper;
        this.planSectionMapper = planSectionMapper;
        this.regionMapper = regionMapper;
        this.entityTypeMapper = entityTypeMapper;
        this.relationTypeMapper = relationTypeMapper;
        this.kgEntityMapper = kgEntityMapper;
        this.kgRelationMapper = kgRelationMapper;
        this.kgTripleMapper = kgTripleMapper;
        this.knowledgeConflictMapper = knowledgeConflictMapper;
        this.knowledgeCompletionMapper = knowledgeCompletionMapper;
        this.documentService = documentService;
        this.aiSupportService = aiSupportService;
        this.operationLogService = operationLogService;
    }

    @Override
    public PageResult<DocumentVO.TaskVO> pageTasks(DocumentDTO.TaskQueryDTO queryDTO) {
        LambdaQueryWrapper<ExtractionTask> wrapper = new LambdaQueryWrapper<ExtractionTask>()
                .eq(queryDTO.getDocumentId() != null, ExtractionTask::getDocumentId, queryDTO.getDocumentId())
                .eq(queryDTO.getTaskStatus() != null, ExtractionTask::getTaskStatus, queryDTO.getTaskStatus())
                .like(StringUtils.hasText(queryDTO.getKeyword()), ExtractionTask::getTaskName, queryDTO.getKeyword())
                .orderByDesc(ExtractionTask::getCreateTime);
        Page<ExtractionTask> page = extractionTaskMapper.selectPage(new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize()), wrapper);
        Map<Long, String> docMap = planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>()).stream().collect(Collectors.toMap(PlanDocument::getId, PlanDocument::getTitle));
        return PageResult.of(page, task -> {
            DocumentVO.TaskVO vo = new DocumentVO.TaskVO();
            vo.setId(task.getId()); vo.setDocumentId(task.getDocumentId()); vo.setDocumentTitle(docMap.get(task.getDocumentId())); vo.setTaskName(task.getTaskName()); vo.setModelName(task.getModelName()); vo.setTaskStatus(task.getTaskStatus()); vo.setTaskStatusText(taskStatusText(task.getTaskStatus())); vo.setProgressPercent(task.getProgressPercent()); vo.setExtractedCount(task.getExtractedCount()); vo.setStartedAt(task.getStartedAt()); vo.setFinishedAt(task.getFinishedAt()); vo.setErrorMessage(task.getErrorMessage()); vo.setCreateTime(task.getCreateTime());
            return vo;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTask(DocumentDTO.TaskSaveDTO dto) {
        PlanDocument document = planDocumentMapper.selectById(dto.getDocumentId());
        if (document == null) {
            throw new BusinessException("预案不存在");
        }
        ExtractionTask task = new ExtractionTask();
        task.setDocumentId(dto.getDocumentId());
        task.setTaskName(dto.getTaskName());
        task.setModelName(StringUtils.hasText(dto.getModelName()) ? dto.getModelName() : "应急抽取助手-Mock");
        task.setPromptTemplate(StringUtils.hasText(dto.getPromptTemplate()) ? dto.getPromptTemplate() : "请抽取主体、任务、资源及关系三元组");
        task.setTaskStatus(0);
        task.setProgressPercent(0);
        task.setExtractedCount(0);
        task.setCreateBy(UserContext.getUserId());
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        extractionTaskMapper.insert(task);
        operationLogService.log("抽取任务", "新增", "创建抽取任务：" + dto.getTaskName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeTask(Long taskId) {
        ExtractionTask task = extractionTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }
        PlanDocument document = planDocumentMapper.selectById(task.getDocumentId());
        if (document == null) {
            throw new BusinessException("预案不存在");
        }
        try {
            if (planSectionMapper.selectCount(new LambdaQueryWrapper<PlanSection>().eq(PlanSection::getDocumentId, document.getId())) == 0) {
                documentService.preprocess(document.getId());
            }
            task.setTaskStatus(1);
            task.setProgressPercent(15);
            task.setStartedAt(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            extractionTaskMapper.updateById(task);

            List<PlanSection> sections = planSectionMapper.selectList(new LambdaQueryWrapper<PlanSection>().eq(PlanSection::getDocumentId, document.getId()).orderByAsc(PlanSection::getSectionNo));
            String regionName = Optional.ofNullable(regionMapper.selectById(document.getRegionId())).map(Region::getRegionName).orElse("目标区域");
            String prompt = buildPrompt(document, sections, regionName, task);
            aiSupportService.chat("EXTRACTION", task.getId(), prompt);

            kgTripleMapper.delete(new LambdaQueryWrapper<KgTriple>().eq(KgTriple::getSourceDocumentId, document.getId()));
            kgRelationMapper.delete(new LambdaQueryWrapper<KgRelation>().eq(KgRelation::getSourceDocumentId, document.getId()));
            knowledgeCompletionMapper.delete(new LambdaQueryWrapper<KnowledgeCompletion>().eq(KnowledgeCompletion::getDocumentId, document.getId()));

            List<RuleTriple> triples = buildRuleTriples(regionName, document);
            Map<String, Long> typeIdMap = entityTypeMapper.selectList(new LambdaQueryWrapper<EntityType>()).stream().collect(Collectors.toMap(EntityType::getTypeCode, EntityType::getId, (a, b) -> a));
            Map<String, Long> relationTypeMap = relationTypeMapper.selectList(new LambdaQueryWrapper<RelationType>()).stream().collect(Collectors.toMap(RelationType::getRelationName, RelationType::getId, (a, b) -> a));

            int count = 0;
            for (RuleTriple triple : triples) {
                Long subjectId = findOrCreateEntity(triple.subject(), triple.subjectType(), document.getRegionId(), document.getId(), typeIdMap);
                Long objectId = findOrCreateEntity(triple.object(), triple.objectType(), document.getRegionId(), document.getId(), typeIdMap);
                KgRelation relation = new KgRelation();
                relation.setSubjectEntityId(subjectId);
                relation.setRelationTypeId(relationTypeMap.getOrDefault(triple.predicate(), null));
                relation.setObjectEntityId(objectId);
                relation.setSourceDocumentId(document.getId());
                relation.setRelationDesc(triple.predicate());
                relation.setConfidence(BigDecimal.valueOf(0.92));
                relation.setCreateTime(LocalDateTime.now());
                relation.setUpdateTime(LocalDateTime.now());
                kgRelationMapper.insert(relation);

                KgTriple kgTriple = new KgTriple();
                kgTriple.setSubjectName(triple.subject());
                kgTriple.setPredicateName(triple.predicate());
                kgTriple.setObjectName(triple.object());
                kgTriple.setSubjectEntityId(subjectId);
                kgTriple.setRelationId(relation.getId());
                kgTriple.setObjectEntityId(objectId);
                kgTriple.setSourceDocumentId(document.getId());
                kgTriple.setConfidence(BigDecimal.valueOf(0.92));
                kgTriple.setValidationStatus(1);
                kgTriple.setCreateTime(LocalDateTime.now());
                kgTriple.setUpdateTime(LocalDateTime.now());
                kgTripleMapper.insert(kgTriple);
                createConflictIfNeeded(kgTriple);
                count++;
            }
            createDefaultCompletions(document, regionName);

            task.setTaskStatus(2);
            task.setProgressPercent(100);
            task.setExtractedCount(count);
            task.setFinishedAt(LocalDateTime.now());
            task.setErrorMessage(null);
            task.setUpdateTime(LocalDateTime.now());
            extractionTaskMapper.updateById(task);
            document.setExtractionStatus(3);
            document.setUpdateTime(LocalDateTime.now());
            planDocumentMapper.updateById(document);
            operationLogService.log("抽取任务", "执行", "执行抽取任务：" + task.getTaskName() + "，生成三元组 " + count + " 条");
        } catch (Exception ex) {
            task.setTaskStatus(3);
            task.setProgressPercent(100);
            task.setErrorMessage(ex.getMessage());
            task.setFinishedAt(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            extractionTaskMapper.updateById(task);
            document.setExtractionStatus(4);
            document.setUpdateTime(LocalDateTime.now());
            planDocumentMapper.updateById(document);
            throw ex;
        }
    }

    private String buildPrompt(PlanDocument document, List<PlanSection> sections, String regionName, ExtractionTask task) {
        String preview = sections.stream().limit(3).map(PlanSection::getSectionContent).collect(Collectors.joining("\n"));
        return "你是应急预案知识抽取助手。请从预案中抽取主体、任务、资源与关系。" + "\n区域：" + regionName + "\n标题：" + document.getTitle() + "\n任务模板：" + task.getPromptTemplate() + "\n文本片段：\n" + preview;
    }

    private List<RuleTriple> buildRuleTriples(String regionName, PlanDocument document) {
        List<RuleTriple> triples = new ArrayList<>();
        triples.add(new RuleTriple(regionName + "防汛指挥部", "机构", "负责", "预警发布", "任务"));
        triples.add(new RuleTriple(regionName + "应急管理厅", "机构", "负责", "人员转移", "任务"));
        triples.add(new RuleTriple(regionName + "气象局", "机构", "协同", regionName + "防汛指挥部", "机构"));
        triples.add(new RuleTriple(regionName + "水利厅", "机构", "调用", "防汛物资仓库", "资源"));
        triples.add(new RuleTriple("预警发布", "任务", "前置", "风险研判", "任务"));
        triples.add(new RuleTriple("人员转移", "任务", "调用", "冲锋舟", "资源"));
        if (StringUtils.hasText(document.getContent()) && document.getContent().contains("沙袋")) {
            triples.add(new RuleTriple(regionName + "应急管理厅", "机构", "调用", "沙袋", "资源"));
        }
        return triples;
    }

    private Long findOrCreateEntity(String name, String typeName, Long regionId, Long documentId, Map<String, Long> typeIdMap) {
        Long entityTypeId = typeIdMap.getOrDefault(typeName, typeIdMap.values().stream().findFirst().orElse(null));
        KgEntity existing = kgEntityMapper.selectOne(new LambdaQueryWrapper<KgEntity>().eq(KgEntity::getEntityName, name).eq(KgEntity::getEntityTypeId, entityTypeId).last("limit 1"));
        if (existing != null) {
            return existing.getId();
        }
        KgEntity entity = new KgEntity();
        entity.setEntityName(name);
        entity.setEntityTypeId(entityTypeId);
        entity.setRegionId(regionId);
        entity.setSourceDocumentId(documentId);
        entity.setDescription("由抽取任务自动生成");
        entity.setConfidence(BigDecimal.valueOf(0.90));
        entity.setStatus(1);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        kgEntityMapper.insert(entity);
        return entity.getId();
    }

    private void createConflictIfNeeded(KgTriple current) {
        KgTriple existed = kgTripleMapper.selectOne(new LambdaQueryWrapper<KgTriple>()
                .eq(KgTriple::getSubjectName, current.getSubjectName())
                .eq(KgTriple::getPredicateName, current.getPredicateName())
                .ne(KgTriple::getObjectName, current.getObjectName())
                .ne(KgTriple::getSourceDocumentId, current.getSourceDocumentId())
                .last("limit 1"));
        if (existed != null) {
            KnowledgeConflict conflict = new KnowledgeConflict();
            conflict.setTripleId(current.getId());
            conflict.setConflictType("对象冲突");
            conflict.setConflictDesc("同一主体对同一谓词出现不同客体：" + current.getSubjectName() + " - " + current.getPredicateName());
            conflict.setStatus(0);
            conflict.setSuggestedResolution("建议人工核验来源预案版本并保留最新生效条目");
            conflict.setCreateTime(LocalDateTime.now());
            conflict.setUpdateTime(LocalDateTime.now());
            knowledgeConflictMapper.insert(conflict);
        }
    }

    private void createDefaultCompletions(PlanDocument document, String regionName) {
        createCompletion(document.getId(), "知识补全", regionName + "通信保障组", "负责", "应急通信保障", "建议补充通信保障主体及任务衔接关系");
        createCompletion(document.getId(), "知识补全", regionName + "防汛指挥部", "调用", "应急照明设备", "建议补充夜间救援保障资源");
    }

    private void createCompletion(Long documentId, String type, String subject, String predicate, String object, String suggestion) {
        KnowledgeCompletion completion = new KnowledgeCompletion();
        completion.setDocumentId(documentId);
        completion.setCompletionType(type);
        completion.setMissingSubject(subject);
        completion.setMissingPredicate(predicate);
        completion.setMissingObject(object);
        completion.setSuggestionContent(suggestion);
        completion.setStatus(0);
        completion.setCreateTime(LocalDateTime.now());
        completion.setUpdateTime(LocalDateTime.now());
        knowledgeCompletionMapper.insert(completion);
    }

    private String taskStatusText(Integer status) {
        return switch (status == null ? -1 : status) {
            case 0 -> "待执行";
            case 1 -> "执行中";
            case 2 -> "已完成";
            case 3 -> "失败";
            default -> "未知";
        };
    }

    private record RuleTriple(String subject, String subjectType, String predicate, String object, String objectType) {}
}
