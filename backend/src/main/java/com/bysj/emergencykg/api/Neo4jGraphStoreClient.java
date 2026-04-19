package com.bysj.emergencykg.api;

import com.bysj.emergencykg.model.vo.KgVO;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class Neo4jGraphStoreClient implements GraphStoreClient {
    private static final String ENTITY_LABEL = "EmergencyEntity";
    private static final String RELATION_TYPE = "EMERGENCY_RELATION";

    private final Driver driver;
    private final MysqlGraphStoreClient mysqlGraphStoreClient;

    public Neo4jGraphStoreClient(@Nullable Driver driver, MysqlGraphStoreClient mysqlGraphStoreClient) {
        this.driver = driver;
        this.mysqlGraphStoreClient = mysqlGraphStoreClient;
    }

    @Override
    public boolean available() {
        return driver != null;
    }

    @Override
    public KgVO.GraphVO queryGraph(Long regionId, Long documentId, String keyword) {
        if (!available()) {
            return mysqlGraphStoreClient.queryGraph(regionId, documentId, keyword);
        }
        String cypher = "MATCH (s:" + ENTITY_LABEL + ")-[r:" + RELATION_TYPE + "]->(o:" + ENTITY_LABEL + ") "
                + "WHERE ($regionId IS NULL OR r.regionId = $regionId) "
                + "AND ($documentId IS NULL OR r.documentId = $documentId) "
                + "AND ($keyword = '' OR s.entityName CONTAINS $keyword OR r.predicate CONTAINS $keyword OR o.entityName CONTAINS $keyword) "
                + "RETURN s.entityName AS source, coalesce(s.category, '实体') AS sourceCategory, "
                + "o.entityName AS target, coalesce(o.category, '实体') AS targetCategory, "
                + "r.predicate AS predicate, r.documentId AS sourceId "
                + "ORDER BY r.documentId, r.tripleId LIMIT 240";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("regionId", regionId);
        params.put("documentId", documentId);
        params.put("keyword", StringUtils.hasText(keyword) ? keyword.trim() : "");
        try (Session session = driver.session()) {
            List<Record> records = session.run(cypher, params).list();
            KgVO.GraphVO vo = new KgVO.GraphVO();
            Map<String, KgVO.GraphNodeVO> nodes = new LinkedHashMap<>();
            for (Record record : records) {
                String source = record.get("source").asString("");
                String target = record.get("target").asString("");
                if (!StringUtils.hasText(source) || !StringUtils.hasText(target)) {
                    continue;
                }
                nodes.computeIfAbsent(source, key -> node(key, record.get("sourceCategory").asString("实体"), record.get("sourceId").isNull() ? null : record.get("sourceId").asLong()));
                nodes.computeIfAbsent(target, key -> node(key, record.get("targetCategory").asString("实体"), record.get("sourceId").isNull() ? null : record.get("sourceId").asLong()));
                KgVO.GraphLinkVO link = new KgVO.GraphLinkVO();
                link.setSource(source);
                link.setTarget(target);
                link.setValue(record.get("predicate").asString(""));
                vo.getLinks().add(link);
            }
            vo.setNodes(new ArrayList<>(nodes.values()));
            vo.setCypherRows(records.stream().limit(20).map(this::toMap).collect(Collectors.toList()));
            if (vo.getLinks().isEmpty()) {
                KgVO.GraphVO fallback = mysqlGraphStoreClient.queryGraph(regionId, documentId, keyword);
                fallback.setMessage("Neo4j 当前暂无已同步图谱快照，已自动回退；" + fallback.getMessage());
                return fallback;
            }
            vo.setMessage("当前使用 Neo4j 图数据库查询，共返回 " + vo.getNodes().size() + " 个节点、" + vo.getLinks().size() + " 条关系");
            return vo;
        } catch (Exception ex) {
            KgVO.GraphVO fallback = mysqlGraphStoreClient.queryGraph(regionId, documentId, keyword);
            fallback.setMessage("Neo4j 图查询失败，已自动回退；" + fallback.getMessage() + "；原因：" + ex.getMessage());
            return fallback;
        }
    }

    @Override
    public Map<String, Object> executeCypher(String cypher) {
        if (!available()) {
            return mysqlGraphStoreClient.executeCypher(cypher);
        }
        try (Session session = driver.session()) {
            List<Map<String, Object>> rows = session.run(cypher).list(this::toMap);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("mode", "neo4j");
            result.put("message", "Cypher 执行成功");
            result.put("rows", rows);
            return result;
        } catch (Exception ex) {
            Map<String, Object> fallback = mysqlGraphStoreClient.executeCypher(cypher);
            fallback.put("message", "Neo4j 执行失败，已自动降级：" + ex.getMessage());
            return fallback;
        }
    }

    @Override
    public void syncVersion(Long versionId, List<KgVO.TripleVO> triples) {
        if (!available() || triples == null || triples.isEmpty()) {
            return;
        }
        List<Map<String, Object>> rows = triples.stream().map(this::toSyncRow).collect(Collectors.toList());
        try (Session session = driver.session()) {
            session.run("CREATE INDEX emergency_entity_name IF NOT EXISTS FOR (n:" + ENTITY_LABEL + ") ON (n.entityName)");
            session.run("CREATE INDEX emergency_relation_version IF NOT EXISTS FOR ()-[r:" + RELATION_TYPE + "]-() ON (r.versionId)");
            session.executeWrite(tx -> {
                tx.run("MATCH (n:" + ENTITY_LABEL + ") DETACH DELETE n");
                tx.run("UNWIND $rows AS row "
                                + "MERGE (s:" + ENTITY_LABEL + " {entityName: row.subjectName}) "
                                + "SET s.category = row.subjectCategory, s.versionId = row.versionId, s.updatedAt = datetime() "
                                + "MERGE (o:" + ENTITY_LABEL + " {entityName: row.objectName}) "
                                + "SET o.category = row.objectCategory, o.versionId = row.versionId, o.updatedAt = datetime() "
                                + "MERGE (s)-[r:" + RELATION_TYPE + " {tripleId: row.tripleId}]->(o) "
                                + "SET r.predicate = row.predicate, r.regionId = row.regionId, r.regionName = row.regionName, "
                                + "r.documentId = row.documentId, r.documentTitle = row.documentTitle, "
                                + "r.versionId = row.versionId, r.updatedAt = datetime()",
                        Collections.singletonMap("rows", rows));
                return null;
            });
        }
    }

    private Map<String, Object> toSyncRow(KgVO.TripleVO triple) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("tripleId", triple.getId());
        row.put("subjectName", triple.getSubjectName());
        row.put("subjectCategory", inferCategory(triple.getSubjectName()));
        row.put("predicate", triple.getPredicateName());
        row.put("objectName", triple.getObjectName());
        row.put("objectCategory", inferCategory(triple.getObjectName()));
        row.put("regionId", triple.getRegionId());
        row.put("regionName", triple.getRegionName());
        row.put("documentId", triple.getSourceDocumentId());
        row.put("documentTitle", triple.getSourceDocumentTitle());
        row.put("versionId", triple.getVersionId());
        return row;
    }

    private KgVO.GraphNodeVO node(String name, String category, Long sourceId) {
        KgVO.GraphNodeVO node = new KgVO.GraphNodeVO();
        node.setId(name);
        node.setName(name);
        node.setCategory(StringUtils.hasText(category) ? category : "实体");
        node.setSourceId(sourceId);
        node.setSymbolSize("机构".equals(category) ? 56 : "资源".equals(category) ? 44 : 48);
        return node;
    }

    private String inferCategory(String name) {
        if (!StringUtils.hasText(name)) {
            return "实体";
        }
        if (name.contains("部门") || name.contains("局") || name.contains("厅") || name.contains("指挥部") || name.contains("中心")) {
            return "机构";
        }
        if (name.contains("设备") || name.contains("物资") || name.contains("泵站") || name.contains("沙袋") || name.contains("冲锋舟") || name.contains("无人机")) {
            return "资源";
        }
        return "任务";
    }

    private Map<String, Object> toMap(Record record) {
        return record.keys().stream().collect(Collectors.toMap(key -> key, key -> convertValue(record.get(key)), (a, b) -> a, LinkedHashMap::new));
    }

    private Object convertValue(Value value) {
        if (value == null || value.isNull()) {
            return null;
        }
        TypeSystem typeSystem = TypeSystem.getDefault();
        if (value.hasType(typeSystem.NODE())) {
            return toNodeMap(value.asNode());
        }
        if (value.hasType(typeSystem.RELATIONSHIP())) {
            return toRelationshipMap(value.asRelationship());
        }
        if (value.hasType(typeSystem.PATH())) {
            return toPathMap(value.asPath());
        }
        if (value.hasType(typeSystem.LIST())) {
            return value.asList(this::convertValue);
        }
        if (value.hasType(typeSystem.MAP())) {
            return value.asMap(this::convertValue);
        }
        return value.asObject();
    }

    private Map<String, Object> toNodeMap(Node node) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("elementId", node.elementId());
        map.put("labels", StreamSupport.stream(node.labels().spliterator(), false).collect(Collectors.toList()));
        map.put("properties", node.asMap(this::convertValue));
        return map;
    }

    private Map<String, Object> toRelationshipMap(Relationship relationship) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("elementId", relationship.elementId());
        map.put("type", relationship.type());
        map.put("startNodeElementId", relationship.startNodeElementId());
        map.put("endNodeElementId", relationship.endNodeElementId());
        map.put("properties", relationship.asMap(this::convertValue));
        return map;
    }

    private Map<String, Object> toPathMap(Path path) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<Map<String, Object>> segments = new ArrayList<>();
        for (Path.Segment segment : path) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("start", toNodeMap(segment.start()));
            item.put("relationship", toRelationshipMap(segment.relationship()));
            item.put("end", toNodeMap(segment.end()));
            segments.add(item);
        }
        map.put("segments", segments);
        return map;
    }
}
