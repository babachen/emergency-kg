package com.bysj.emergencykg.api;

import com.bysj.emergencykg.model.vo.KgVO;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Neo4jGraphStoreClient implements GraphStoreClient {
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
        KgVO.GraphVO vo = mysqlGraphStoreClient.queryGraph(regionId, documentId, keyword);
        if (available()) {
            vo.setMessage(vo.getMessage() + "；Neo4j 已启用，可切换为真实 Cypher 查询");
        }
        return vo;
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
    }

    private Map<String, Object> toMap(Record record) {
        return record.keys().stream().collect(Collectors.toMap(key -> key, record::get, (a, b) -> a, LinkedHashMap::new));
    }
}
