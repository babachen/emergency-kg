package com.bysj.emergencykg.api;

import com.bysj.emergencykg.model.vo.KgVO;

import java.util.List;
import java.util.Map;

public interface GraphStoreClient {
    boolean available();
    KgVO.GraphVO queryGraph(Long regionId, Long documentId, String keyword);
    Map<String, Object> executeCypher(String cypher);
    void syncVersion(Long versionId, List<KgVO.TripleVO> triples);
}
