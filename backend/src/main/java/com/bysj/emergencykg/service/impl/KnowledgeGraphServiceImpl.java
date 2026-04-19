package com.bysj.emergencykg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bysj.emergencykg.api.MysqlGraphStoreClient;
import com.bysj.emergencykg.api.Neo4jGraphStoreClient;
import com.bysj.emergencykg.common.PageQuery;
import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.mapper.*;
import com.bysj.emergencykg.model.dto.KgDTO;
import com.bysj.emergencykg.model.entity.*;
import com.bysj.emergencykg.model.vo.KgVO;
import com.bysj.emergencykg.service.KnowledgeGraphService;
import com.bysj.emergencykg.service.OperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KnowledgeGraphServiceImpl implements KnowledgeGraphService {
    private final EntityTypeMapper entityTypeMapper;
    private final RelationTypeMapper relationTypeMapper;
    private final KgEntityMapper kgEntityMapper;
    private final KgTripleMapper kgTripleMapper;
    private final KgRelationMapper kgRelationMapper;
    private final PlanDocumentMapper planDocumentMapper;
    private final RegionMapper regionMapper;
    private final KnowledgeConflictMapper knowledgeConflictMapper;
    private final KnowledgeCompletionMapper knowledgeCompletionMapper;
    private final GraphVersionMapper graphVersionMapper;
    private final MysqlGraphStoreClient mysqlGraphStoreClient;
    private final Neo4jGraphStoreClient neo4jGraphStoreClient;
    private final AiSupportService aiSupportService;
    private final OperationLogService operationLogService;

    public KnowledgeGraphServiceImpl(EntityTypeMapper entityTypeMapper, RelationTypeMapper relationTypeMapper, KgEntityMapper kgEntityMapper, KgTripleMapper kgTripleMapper, KgRelationMapper kgRelationMapper, PlanDocumentMapper planDocumentMapper, RegionMapper regionMapper, KnowledgeConflictMapper knowledgeConflictMapper, KnowledgeCompletionMapper knowledgeCompletionMapper, GraphVersionMapper graphVersionMapper, MysqlGraphStoreClient mysqlGraphStoreClient, Neo4jGraphStoreClient neo4jGraphStoreClient, AiSupportService aiSupportService, OperationLogService operationLogService) {
        this.entityTypeMapper = entityTypeMapper;
        this.relationTypeMapper = relationTypeMapper;
        this.kgEntityMapper = kgEntityMapper;
        this.kgTripleMapper = kgTripleMapper;
        this.kgRelationMapper = kgRelationMapper;
        this.planDocumentMapper = planDocumentMapper;
        this.regionMapper = regionMapper;
        this.knowledgeConflictMapper = knowledgeConflictMapper;
        this.knowledgeCompletionMapper = knowledgeCompletionMapper;
        this.graphVersionMapper = graphVersionMapper;
        this.mysqlGraphStoreClient = mysqlGraphStoreClient;
        this.neo4jGraphStoreClient = neo4jGraphStoreClient;
        this.aiSupportService = aiSupportService;
        this.operationLogService = operationLogService;
    }

    @Override
    public List<KgVO.OntologyVO> listEntityTypes() {
        return entityTypeMapper.selectList(new LambdaQueryWrapper<EntityType>().orderByAsc(EntityType::getId)).stream().map(this::toEntityTypeVO).collect(Collectors.toList());
    }

    @Override
    public List<KgVO.OntologyVO> listRelationTypes() {
        return relationTypeMapper.selectList(new LambdaQueryWrapper<RelationType>().orderByAsc(RelationType::getId)).stream().map(this::toRelationTypeVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntityType(KgDTO.OntologySaveDTO dto) {
        EntityType entityType = new EntityType();
        entityType.setTypeCode(dto.getCode()); entityType.setTypeName(dto.getName()); entityType.setColor(dto.getColor()); entityType.setDescription(dto.getDescription()); entityType.setCreateTime(LocalDateTime.now()); entityType.setUpdateTime(LocalDateTime.now());
        entityTypeMapper.insert(entityType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntityType(Long id, KgDTO.OntologySaveDTO dto) {
        EntityType entityType = entityTypeMapper.selectById(id); if (entityType == null) throw new BusinessException("实体类型不存在");
        entityType.setTypeCode(dto.getCode()); entityType.setTypeName(dto.getName()); entityType.setColor(dto.getColor()); entityType.setDescription(dto.getDescription()); entityType.setUpdateTime(LocalDateTime.now());
        entityTypeMapper.updateById(entityType);
    }

    @Override
    public void deleteEntityType(Long id) { entityTypeMapper.deleteById(id); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRelationType(KgDTO.OntologySaveDTO dto) {
        RelationType relationType = new RelationType();
        relationType.setRelationCode(dto.getCode()); relationType.setRelationName(dto.getName()); relationType.setDescription(dto.getDescription()); relationType.setDirectionDesc(dto.getColor()); relationType.setCreateTime(LocalDateTime.now()); relationType.setUpdateTime(LocalDateTime.now());
        relationTypeMapper.insert(relationType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRelationType(Long id, KgDTO.OntologySaveDTO dto) {
        RelationType relationType = relationTypeMapper.selectById(id); if (relationType == null) throw new BusinessException("关系类型不存在");
        relationType.setRelationCode(dto.getCode()); relationType.setRelationName(dto.getName()); relationType.setDescription(dto.getDescription()); relationType.setDirectionDesc(dto.getColor()); relationType.setUpdateTime(LocalDateTime.now());
        relationTypeMapper.updateById(relationType);
    }

    @Override
    public void deleteRelationType(Long id) { relationTypeMapper.deleteById(id); }

    @Override
    public PageResult<KgVO.EntityVO> pageEntities(KgDTO.EntityQueryDTO queryDTO) {
        LambdaQueryWrapper<KgEntity> wrapper = new LambdaQueryWrapper<KgEntity>()
                .eq(queryDTO.getRegionId() != null, KgEntity::getRegionId, queryDTO.getRegionId())
                .eq(queryDTO.getEntityTypeId() != null, KgEntity::getEntityTypeId, queryDTO.getEntityTypeId())
                .like(StringUtils.hasText(queryDTO.getKeyword()), KgEntity::getEntityName, queryDTO.getKeyword())
                .orderByDesc(KgEntity::getCreateTime);
        Page<KgEntity> page = kgEntityMapper.selectPage(new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize()), wrapper);
        Map<Long, String> typeMap = entityTypeMapper.selectList(new LambdaQueryWrapper<EntityType>()).stream().collect(Collectors.toMap(EntityType::getId, EntityType::getTypeName));
        Map<Long, String> regionMap = regionMapper.selectList(new LambdaQueryWrapper<Region>()).stream().collect(Collectors.toMap(Region::getId, Region::getRegionName));
        Map<Long, String> docMap = planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>()).stream().collect(Collectors.toMap(PlanDocument::getId, PlanDocument::getTitle));
        return PageResult.of(page, entity -> {
            KgVO.EntityVO vo = new KgVO.EntityVO();
            vo.setId(entity.getId()); vo.setEntityName(entity.getEntityName()); vo.setEntityTypeId(entity.getEntityTypeId()); vo.setEntityTypeName(typeMap.get(entity.getEntityTypeId())); vo.setRegionId(entity.getRegionId()); vo.setRegionName(regionMap.get(entity.getRegionId())); vo.setSourceDocumentId(entity.getSourceDocumentId()); vo.setSourceDocumentTitle(docMap.get(entity.getSourceDocumentId())); vo.setDescription(entity.getDescription()); vo.setConfidence(entity.getConfidence()); vo.setStatus(entity.getStatus()); vo.setStatusText(entity.getStatus() != null && entity.getStatus() == 1 ? "有效" : "禁用"); vo.setCreateTime(entity.getCreateTime());
            return vo;
        });
    }

    @Override
    public PageResult<KgVO.TripleVO> pageTriples(KgDTO.TripleQueryDTO queryDTO) {
        Set<Long> docIds = null;
        if (queryDTO.getRegionId() != null) {
            docIds = planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>().eq(PlanDocument::getRegionId, queryDTO.getRegionId())).stream().map(PlanDocument::getId).collect(Collectors.toSet());
        }
        LambdaQueryWrapper<KgTriple> wrapper = new LambdaQueryWrapper<KgTriple>()
                .eq(queryDTO.getDocumentId() != null, KgTriple::getSourceDocumentId, queryDTO.getDocumentId())
                .in(docIds != null && !docIds.isEmpty(), KgTriple::getSourceDocumentId, docIds)
                .eq(queryDTO.getValidationStatus() != null, KgTriple::getValidationStatus, queryDTO.getValidationStatus())
                .and(StringUtils.hasText(queryDTO.getKeyword()), w -> w.like(KgTriple::getSubjectName, queryDTO.getKeyword()).or().like(KgTriple::getPredicateName, queryDTO.getKeyword()).or().like(KgTriple::getObjectName, queryDTO.getKeyword()))
                .orderByDesc(KgTriple::getCreateTime);
        Page<KgTriple> page = kgTripleMapper.selectPage(new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize()), wrapper);
        Map<Long, PlanDocument> docMap = loadDocumentMap();
        Map<Long, String> regionMap = loadRegionNameMap();
        Map<Long, String> versionMap = graphVersionMapper.selectList(new LambdaQueryWrapper<GraphVersion>()).stream().collect(Collectors.toMap(GraphVersion::getId, GraphVersion::getVersionName));
        return PageResult.of(page, triple -> buildTripleVO(triple, docMap, regionMap, versionMap));
    }

    @Override
    public KgVO.GraphVO graphView(KgDTO.GraphQueryDTO queryDTO) {
        return graphClient().queryGraph(queryDTO.getRegionId(), queryDTO.getDocumentId(), queryDTO.getKeyword());
    }

    @Override
    public Map<String, Object> executeCypher(KgDTO.CypherQueryDTO queryDTO) {
        return graphClient().executeCypher(queryDTO.getCypher());
    }

    @Override
    public KgVO.QaResponseVO askQuestion(KgDTO.QaRequestDTO requestDTO) {
        List<KgTriple> triples = queryRelevantTriples(requestDTO.getQuestion(), requestDTO.getDocumentId(), requestDTO.getRegionId());
        KgVO.QaResponseVO vo = new KgVO.QaResponseVO();
        vo.setReferences(triples.stream().limit(5).map(triple -> triple.getSubjectName() + " - " + triple.getPredicateName() + " - " + triple.getObjectName()).collect(Collectors.toList()));
        if (vo.getReferences().isEmpty()) {
            vo.setAnswer("当前知识图谱中未检索到与问题直接匹配的知识，建议先执行抽取任务或补充预案内容。");
        } else {
            String prompt = "请基于以下应急预案知识回答问题：\n问题：" + requestDTO.getQuestion() + "\n知识：\n" + String.join("\n", vo.getReferences());
            String aiAnswer = aiSupportService.chat("QA", requestDTO.getDocumentId(), prompt);
            vo.setAnswer(StringUtils.hasText(aiAnswer) ? aiAnswer : buildQaText(vo.getReferences()));
        }
        vo.setModelName(aiSupportService.useBysj() ? "bysj-chat-api" : "Mock 推理引擎");
        return vo;
    }

    @Override
    public KgVO.QaResponseVO reasoning(KgDTO.QaRequestDTO requestDTO) {
        List<KgTriple> triples = queryRelevantTriples(requestDTO.getQuestion(), requestDTO.getDocumentId(), requestDTO.getRegionId());
        KgVO.QaResponseVO vo = new KgVO.QaResponseVO();
        if (triples.isEmpty()) {
            vo.setAnswer("当前没有可用于推理的图谱路径，请先补充抽取数据。");
            vo.setModelName("规则推理");
            return vo;
        }
        vo.setReasoningChain(triples.stream().limit(3).map(t -> t.getSubjectName() + " -> " + t.getPredicateName() + " -> " + t.getObjectName()).collect(Collectors.toList()));
        vo.setReferences(vo.getReasoningChain());
        vo.setAnswer("推理结果：根据已抽取的路径链，可判断相关责任主体、前置任务和调用资源之间存在明确协同关系。建议优先检查：" + vo.getReasoningChain().get(0));
        vo.setModelName("规则推理");
        return vo;
    }

    @Override
    public PageResult<KgVO.ConflictVO> pageConflicts(PageQuery pageQuery) {
        Page<KnowledgeConflict> page = knowledgeConflictMapper.selectPage(new Page<>(pageQuery.getCurrent(), pageQuery.getPageSize()), new LambdaQueryWrapper<KnowledgeConflict>().orderByDesc(KnowledgeConflict::getCreateTime));
        return PageResult.of(page, item -> { KgVO.ConflictVO vo = new KgVO.ConflictVO(); vo.setId(item.getId()); vo.setTripleId(item.getTripleId()); vo.setConflictType(item.getConflictType()); vo.setConflictDesc(item.getConflictDesc()); vo.setStatus(item.getStatus()); vo.setStatusText(item.getStatus() != null && item.getStatus() == 1 ? "已处理" : "待处理"); vo.setSuggestedResolution(item.getSuggestedResolution()); vo.setCreateTime(item.getCreateTime()); return vo; });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConflictStatus(Long id, Integer status) {
        KnowledgeConflict conflict = knowledgeConflictMapper.selectById(id); if (conflict == null) throw new BusinessException("冲突记录不存在");
        conflict.setStatus(status); conflict.setUpdateTime(LocalDateTime.now()); knowledgeConflictMapper.updateById(conflict);
    }

    @Override
    public PageResult<KgVO.CompletionVO> pageCompletions(PageQuery pageQuery) {
        Page<KnowledgeCompletion> page = knowledgeCompletionMapper.selectPage(new Page<>(pageQuery.getCurrent(), pageQuery.getPageSize()), new LambdaQueryWrapper<KnowledgeCompletion>().orderByDesc(KnowledgeCompletion::getCreateTime));
        Map<Long, String> docMap = planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>()).stream().collect(Collectors.toMap(PlanDocument::getId, PlanDocument::getTitle));
        return PageResult.of(page, item -> { KgVO.CompletionVO vo = new KgVO.CompletionVO(); vo.setId(item.getId()); vo.setDocumentId(item.getDocumentId()); vo.setDocumentTitle(docMap.get(item.getDocumentId())); vo.setCompletionType(item.getCompletionType()); vo.setMissingSubject(item.getMissingSubject()); vo.setMissingPredicate(item.getMissingPredicate()); vo.setMissingObject(item.getMissingObject()); vo.setSuggestionContent(item.getSuggestionContent()); vo.setStatus(item.getStatus()); vo.setStatusText(item.getStatus() != null && item.getStatus() == 1 ? "已应用" : "待应用"); vo.setApplyResult(item.getApplyResult()); vo.setCreateTime(item.getCreateTime()); return vo; });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyCompletion(Long id) {
        KnowledgeCompletion completion = knowledgeCompletionMapper.selectById(id); if (completion == null) throw new BusinessException("补全记录不存在");
        if (completion.getStatus() != null && completion.getStatus() == 1) { return; }
        Map<String, Long> typeIdMap = entityTypeMapper.selectList(new LambdaQueryWrapper<EntityType>()).stream().collect(Collectors.toMap(EntityType::getTypeCode, EntityType::getId, (a, b) -> a));
        PlanDocument document = planDocumentMapper.selectById(completion.getDocumentId());
        Long subjectId = findOrCreateEntity(completion.getMissingSubject(), inferType(completion.getMissingSubject(), true), document.getRegionId(), document.getId(), typeIdMap);
        Long objectId = findOrCreateEntity(completion.getMissingObject(), inferType(completion.getMissingObject(), false), document.getRegionId(), document.getId(), typeIdMap);
        KgRelation relation = new KgRelation();
        relation.setSubjectEntityId(subjectId); relation.setObjectEntityId(objectId); relation.setSourceDocumentId(document.getId()); relation.setRelationDesc(completion.getMissingPredicate()); relation.setConfidence(BigDecimal.valueOf(0.88)); relation.setCreateTime(LocalDateTime.now()); relation.setUpdateTime(LocalDateTime.now()); kgRelationMapper.insert(relation);
        KgTriple triple = new KgTriple();
        triple.setSubjectName(completion.getMissingSubject()); triple.setPredicateName(completion.getMissingPredicate()); triple.setObjectName(completion.getMissingObject()); triple.setSubjectEntityId(subjectId); triple.setRelationId(relation.getId()); triple.setObjectEntityId(objectId); triple.setSourceDocumentId(document.getId()); triple.setConfidence(BigDecimal.valueOf(0.88)); triple.setValidationStatus(1); triple.setCreateTime(LocalDateTime.now()); triple.setUpdateTime(LocalDateTime.now()); kgTripleMapper.insert(triple);
        completion.setStatus(1); completion.setApplyResult("已生成补全三元组：" + triple.getSubjectName() + " - " + triple.getPredicateName() + " - " + triple.getObjectName()); completion.setUpdateTime(LocalDateTime.now()); knowledgeCompletionMapper.updateById(completion);
        operationLogService.log("知识补全", "应用", completion.getApplyResult());
    }

    @Override
    public PageResult<KgVO.VersionVO> pageVersions(PageQuery pageQuery) {
        Page<GraphVersion> page = graphVersionMapper.selectPage(new Page<>(pageQuery.getCurrent(), pageQuery.getPageSize()), new LambdaQueryWrapper<GraphVersion>().orderByDesc(GraphVersion::getCreateTime));
        return PageResult.of(page, item -> { KgVO.VersionVO vo = new KgVO.VersionVO(); vo.setId(item.getId()); vo.setVersionName(item.getVersionName()); vo.setVersionNo(item.getVersionNo()); vo.setSourceDesc(item.getSourceDesc()); vo.setNodeCount(item.getNodeCount()); vo.setRelationCount(item.getRelationCount()); vo.setTripleCount(item.getTripleCount()); vo.setQualityScore(item.getQualityScore()); vo.setPublishedStatus(item.getPublishedStatus()); vo.setPublishedStatusText(item.getPublishedStatus() != null && item.getPublishedStatus() == 1 ? "当前版本" : item.getPublishedStatus() != null && item.getPublishedStatus() == 2 ? "已归档" : "草稿"); vo.setCreateTime(item.getCreateTime()); return vo; });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createVersion(KgDTO.VersionSaveDTO dto) {
        graphVersionMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<GraphVersion>().eq(GraphVersion::getPublishedStatus, 1).set(GraphVersion::getPublishedStatus, 2).set(GraphVersion::getUpdateTime, LocalDateTime.now()));
        GraphVersion version = new GraphVersion();
        version.setVersionName(dto.getVersionName()); version.setVersionNo(dto.getVersionNo()); version.setSourceDesc(dto.getSourceDesc()); version.setNodeCount(Math.toIntExact(kgEntityMapper.selectCount(new LambdaQueryWrapper<>()))); version.setRelationCount(Math.toIntExact(kgRelationMapper.selectCount(new LambdaQueryWrapper<>()))); version.setTripleCount(Math.toIntExact(kgTripleMapper.selectCount(new LambdaQueryWrapper<>()))); version.setQualityScore(BigDecimal.valueOf(Math.min(99, 88 + graphVersionMapper.selectCount(new LambdaQueryWrapper<>())))); version.setPublishedStatus(1); version.setCreateTime(LocalDateTime.now()); version.setUpdateTime(LocalDateTime.now()); graphVersionMapper.insert(version);
        kgTripleMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<KgTriple>().set(KgTriple::getVersionId, version.getId()).set(KgTriple::getUpdateTime, LocalDateTime.now()));
        try {
            syncGraphStoreSnapshot("创建图谱版本：" + dto.getVersionName());
        } catch (Exception ex) {
            operationLogService.log("图谱版本", "同步", "图谱版本已创建，但同步 Neo4j 失败：" + ex.getMessage());
        }
        operationLogService.log("图谱版本", "创建", "创建图谱版本：" + dto.getVersionName());
    }

    @Override
    public void syncGraphStoreSnapshot(String trigger) {
        if (!neo4jGraphStoreClient.available()) {
            return;
        }
        List<KgVO.TripleVO> syncTriples = listSyncTriples();
        if (syncTriples.isEmpty()) {
            operationLogService.log("图谱版本", "同步", "未检测到可同步的图谱三元组，触发方式：" + trigger);
            return;
        }
        neo4jGraphStoreClient.syncVersion(resolveSyncVersionId(), syncTriples);
        operationLogService.log("图谱版本", "同步", "已同步 " + syncTriples.size() + " 条三元组到 Neo4j，触发方式：" + trigger);
    }

    private List<KgTriple> queryRelevantTriples(String question, Long documentId, Long regionId) {
        Set<Long> docIds = null;
        if (regionId != null) {
            docIds = planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>().eq(PlanDocument::getRegionId, regionId)).stream().map(PlanDocument::getId).collect(Collectors.toSet());
        }
        LambdaQueryWrapper<KgTriple> wrapper = new LambdaQueryWrapper<KgTriple>()
                .eq(documentId != null, KgTriple::getSourceDocumentId, documentId)
                .in(docIds != null && !docIds.isEmpty(), KgTriple::getSourceDocumentId, docIds)
                .and(StringUtils.hasText(question), w -> w.like(KgTriple::getSubjectName, question).or().like(KgTriple::getPredicateName, question).or().like(KgTriple::getObjectName, question))
                .orderByDesc(KgTriple::getCreateTime).last("limit 12");
        List<KgTriple> triples = kgTripleMapper.selectList(wrapper);
        if (triples.isEmpty()) {
            triples = kgTripleMapper.selectList(new LambdaQueryWrapper<KgTriple>().orderByDesc(KgTriple::getCreateTime).last("limit 6"));
        }
        return triples;
    }

    private String buildQaText(List<String> references) {
        return "根据当前图谱知识，可确认以下关键链路：" + String.join("；", references);
    }

    private String inferType(String text, boolean subject) {
        if (text.contains("局") || text.contains("厅") || text.contains("组") || text.contains("指挥部")) {
            return "机构";
        }
        if (text.contains("设备") || text.contains("物资") || text.contains("沙袋") || text.contains("冲锋舟")) {
            return "资源";
        }
        return subject ? "机构" : "任务";
    }

    private Long findOrCreateEntity(String name, String typeName, Long regionId, Long documentId, Map<String, Long> typeIdMap) {
        Long entityTypeId = typeIdMap.getOrDefault(typeName, typeIdMap.values().stream().findFirst().orElse(null));
        KgEntity existing = kgEntityMapper.selectOne(new LambdaQueryWrapper<KgEntity>().eq(KgEntity::getEntityName, name).eq(KgEntity::getEntityTypeId, entityTypeId).eq(regionId != null, KgEntity::getRegionId, regionId).last("limit 1"));
        if (existing != null) { return existing.getId(); }
        KgEntity entity = new KgEntity();
        entity.setEntityName(name); entity.setEntityTypeId(entityTypeId); entity.setRegionId(regionId); entity.setSourceDocumentId(documentId); entity.setDescription("由图谱维护动作生成"); entity.setConfidence(BigDecimal.valueOf(0.88)); entity.setStatus(1); entity.setCreateTime(LocalDateTime.now()); entity.setUpdateTime(LocalDateTime.now()); kgEntityMapper.insert(entity);
        return entity.getId();
    }

    private List<KgVO.TripleVO> listSyncTriples() {
        Map<Long, PlanDocument> docMap = loadDocumentMap();
        Map<Long, String> regionMap = loadRegionNameMap();
        Map<Long, String> versionMap = graphVersionMapper.selectList(new LambdaQueryWrapper<GraphVersion>()).stream()
                .collect(Collectors.toMap(GraphVersion::getId, GraphVersion::getVersionName, (a, b) -> a));
        return kgTripleMapper.selectList(new LambdaQueryWrapper<KgTriple>().orderByAsc(KgTriple::getId)).stream()
                .map(triple -> buildTripleVO(triple, docMap, regionMap, versionMap))
                .collect(Collectors.toList());
    }

    private Long resolveSyncVersionId() {
        GraphVersion published = graphVersionMapper.selectOne(new LambdaQueryWrapper<GraphVersion>()
                .eq(GraphVersion::getPublishedStatus, 1)
                .orderByDesc(GraphVersion::getCreateTime)
                .last("limit 1"));
        if (published != null) {
            return published.getId();
        }
        GraphVersion latest = graphVersionMapper.selectOne(new LambdaQueryWrapper<GraphVersion>()
                .orderByDesc(GraphVersion::getCreateTime)
                .last("limit 1"));
        return latest != null ? latest.getId() : null;
    }

    private KgVO.TripleVO buildTripleVO(KgTriple triple, Map<Long, PlanDocument> docMap, Map<Long, String> regionMap, Map<Long, String> versionMap) {
        KgVO.TripleVO vo = new KgVO.TripleVO();
        PlanDocument document = docMap.get(triple.getSourceDocumentId());
        vo.setId(triple.getId());
        vo.setSubjectName(triple.getSubjectName());
        vo.setPredicateName(triple.getPredicateName());
        vo.setObjectName(triple.getObjectName());
        vo.setSourceDocumentId(triple.getSourceDocumentId());
        vo.setSourceDocumentTitle(document != null ? document.getTitle() : null);
        vo.setRegionId(document != null ? document.getRegionId() : null);
        vo.setRegionName(document != null ? regionMap.get(document.getRegionId()) : null);
        vo.setVersionId(triple.getVersionId());
        vo.setVersionName(versionMap.get(triple.getVersionId()));
        vo.setConfidence(triple.getConfidence());
        vo.setValidationStatus(triple.getValidationStatus());
        vo.setValidationStatusText(validationText(triple.getValidationStatus()));
        vo.setCreateTime(triple.getCreateTime());
        return vo;
    }

    private Map<Long, PlanDocument> loadDocumentMap() {
        return planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>()).stream().collect(Collectors.toMap(PlanDocument::getId, document -> document));
    }

    private Map<Long, String> loadRegionNameMap() {
        return regionMapper.selectList(new LambdaQueryWrapper<Region>()).stream().collect(Collectors.toMap(Region::getId, Region::getRegionName));
    }

    private KgVO.OntologyVO toEntityTypeVO(EntityType entityType) {
        KgVO.OntologyVO vo = new KgVO.OntologyVO(); vo.setId(entityType.getId()); vo.setCode(entityType.getTypeCode()); vo.setName(entityType.getTypeName()); vo.setColor(entityType.getColor()); vo.setDescription(entityType.getDescription()); return vo;
    }

    private KgVO.OntologyVO toRelationTypeVO(RelationType relationType) {
        KgVO.OntologyVO vo = new KgVO.OntologyVO(); vo.setId(relationType.getId()); vo.setCode(relationType.getRelationCode()); vo.setName(relationType.getRelationName()); vo.setColor(relationType.getDirectionDesc()); vo.setDescription(relationType.getDescription()); return vo;
    }

    private String validationText(Integer code) {
        return switch (code == null ? -1 : code) {
            case 0 -> "待校验";
            case 1 -> "已确认";
            case 2 -> "存在冲突";
            default -> "未知";
        };
    }

    private com.bysj.emergencykg.api.GraphStoreClient graphClient() {
        return neo4jGraphStoreClient.available() ? neo4jGraphStoreClient : mysqlGraphStoreClient;
    }
}
