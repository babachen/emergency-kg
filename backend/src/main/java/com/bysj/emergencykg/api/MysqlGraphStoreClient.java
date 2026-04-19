package com.bysj.emergencykg.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bysj.emergencykg.mapper.KgTripleMapper;
import com.bysj.emergencykg.mapper.PlanDocumentMapper;
import com.bysj.emergencykg.model.entity.KgTriple;
import com.bysj.emergencykg.model.entity.PlanDocument;
import com.bysj.emergencykg.model.vo.KgVO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MysqlGraphStoreClient implements GraphStoreClient {
    private final KgTripleMapper kgTripleMapper;
    private final PlanDocumentMapper planDocumentMapper;

    public MysqlGraphStoreClient(KgTripleMapper kgTripleMapper, PlanDocumentMapper planDocumentMapper) {
        this.kgTripleMapper = kgTripleMapper;
        this.planDocumentMapper = planDocumentMapper;
    }

    @Override
    public boolean available() {
        return true;
    }

    @Override
    public KgVO.GraphVO queryGraph(Long regionId, Long documentId, String keyword) {
        Set<Long> docIds = null;
        if (regionId != null) {
            docIds = planDocumentMapper.selectList(new LambdaQueryWrapper<PlanDocument>().eq(PlanDocument::getRegionId, regionId)).stream().map(PlanDocument::getId).collect(Collectors.toSet());
            if (docIds.isEmpty()) {
                KgVO.GraphVO empty = new KgVO.GraphVO();
                empty.setMessage("当前区域暂无已入库图谱数据");
                return empty;
            }
        }
        LambdaQueryWrapper<KgTriple> wrapper = new LambdaQueryWrapper<KgTriple>()
                .eq(documentId != null, KgTriple::getSourceDocumentId, documentId)
                .in(docIds != null, KgTriple::getSourceDocumentId, docIds)
                .and(StringUtils.hasText(keyword), w -> w.like(KgTriple::getSubjectName, keyword).or().like(KgTriple::getPredicateName, keyword).or().like(KgTriple::getObjectName, keyword))
                .orderByDesc(KgTriple::getCreateTime).last("limit 120");
        List<KgTriple> triples = kgTripleMapper.selectList(wrapper);
        KgVO.GraphVO vo = new KgVO.GraphVO();
        Map<String, KgVO.GraphNodeVO> nodes = new LinkedHashMap<>();
        for (KgTriple triple : triples) {
            nodes.computeIfAbsent(triple.getSubjectName(), key -> node(key, "实体"));
            nodes.computeIfAbsent(triple.getObjectName(), key -> node(key, "实体"));
            KgVO.GraphLinkVO link = new KgVO.GraphLinkVO();
            link.setSource(triple.getSubjectName());
            link.setTarget(triple.getObjectName());
            link.setValue(triple.getPredicateName());
            vo.getLinks().add(link);
        }
        vo.setNodes(new ArrayList<>(nodes.values()));
        vo.setMessage("当前使用 MySQL 图谱降级查询，共返回 " + vo.getNodes().size() + " 个节点、" + vo.getLinks().size() + " 条关系");
        return vo;
    }

    @Override
    public Map<String, Object> executeCypher(String cypher) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mode", "mysql-fallback");
        result.put("message", "当前未启用 Neo4j，Cypher 语句未真实执行，已返回 MySQL 降级提示");
        result.put("rows", Collections.emptyList());
        result.put("cypher", cypher);
        return result;
    }

    @Override
    public void syncVersion(Long versionId, List<KgVO.TripleVO> triples) {
    }

    private KgVO.GraphNodeVO node(String name, String category) {
        KgVO.GraphNodeVO node = new KgVO.GraphNodeVO();
        node.setId(name);
        node.setName(name);
        node.setCategory(category);
        node.setSourceId(null);
        node.setSymbolSize(48);
        return node;
    }
}
