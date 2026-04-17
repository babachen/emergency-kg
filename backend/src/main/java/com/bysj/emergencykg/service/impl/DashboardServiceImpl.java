package com.bysj.emergencykg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bysj.emergencykg.mapper.*;
import com.bysj.emergencykg.model.entity.*;
import com.bysj.emergencykg.model.vo.DashboardVO;
import com.bysj.emergencykg.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final PlanDocumentMapper planDocumentMapper;
    private final PlanSectionMapper planSectionMapper;
    private final ExtractionTaskMapper extractionTaskMapper;
    private final KgEntityMapper kgEntityMapper;
    private final KgRelationMapper kgRelationMapper;
    private final KgTripleMapper kgTripleMapper;
    private final KnowledgeConflictMapper knowledgeConflictMapper;
    private final KnowledgeCompletionMapper knowledgeCompletionMapper;
    private final GraphVersionMapper graphVersionMapper;
    private final RegionMapper regionMapper;

    public DashboardServiceImpl(PlanDocumentMapper planDocumentMapper, PlanSectionMapper planSectionMapper, ExtractionTaskMapper extractionTaskMapper, KgEntityMapper kgEntityMapper, KgRelationMapper kgRelationMapper, KgTripleMapper kgTripleMapper, KnowledgeConflictMapper knowledgeConflictMapper, KnowledgeCompletionMapper knowledgeCompletionMapper, GraphVersionMapper graphVersionMapper, RegionMapper regionMapper) {
        this.planDocumentMapper = planDocumentMapper;
        this.planSectionMapper = planSectionMapper;
        this.extractionTaskMapper = extractionTaskMapper;
        this.kgEntityMapper = kgEntityMapper;
        this.kgRelationMapper = kgRelationMapper;
        this.kgTripleMapper = kgTripleMapper;
        this.knowledgeConflictMapper = knowledgeConflictMapper;
        this.knowledgeCompletionMapper = knowledgeCompletionMapper;
        this.graphVersionMapper = graphVersionMapper;
        this.regionMapper = regionMapper;
    }

    @Override
    public DashboardVO summary() {
        DashboardVO vo = new DashboardVO();
        vo.setDocumentCount(planDocumentMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setSectionCount(planSectionMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setTaskCount(extractionTaskMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setRunningTaskCount(extractionTaskMapper.selectCount(new LambdaQueryWrapper<ExtractionTask>().eq(ExtractionTask::getTaskStatus, 1)));
        vo.setEntityCount(kgEntityMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setRelationCount(kgRelationMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setTripleCount(kgTripleMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setConflictCount(knowledgeConflictMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setCompletionCount(knowledgeCompletionMapper.selectCount(new LambdaQueryWrapper<>()));
        vo.setVersionCount(graphVersionMapper.selectCount(new LambdaQueryWrapper<>()));

        extractionTaskMapper.selectList(new LambdaQueryWrapper<ExtractionTask>()).stream()
                .collect(Collectors.groupingBy(task -> switch (task.getTaskStatus() == null ? -1 : task.getTaskStatus()) { case 0 -> "待执行"; case 1 -> "执行中"; case 2 -> "已完成"; case 3 -> "失败"; default -> "未知"; }, Collectors.counting()))
                .forEach((k, v) -> vo.getTaskStatusList().add(new DashboardVO.NameValueVO(k, v)));

        Map<Long, String> regionMap = regionMapper.selectList(new LambdaQueryWrapper<Region>().eq(Region::getStatus, 1)).stream().collect(Collectors.toMap(Region::getId, Region::getRegionName, (a, b) -> a));
        planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>()).stream()
                .collect(Collectors.groupingBy(PlanDocument::getRegionId, Collectors.counting()))
                .forEach((k, v) -> vo.getRegionDistribution().add(new DashboardVO.NameValueVO(regionMap.getOrDefault(k, "未知区域"), v)));

        graphVersionMapper.selectList(new LambdaQueryWrapper<GraphVersion>().orderByAsc(GraphVersion::getCreateTime)).stream().limit(8)
                .forEach(item -> vo.getQualityTrend().add(new DashboardVO.NameValueVO(item.getVersionNo(), item.getQualityScore())));
        return vo;
    }
}
