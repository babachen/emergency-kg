package com.bysj.emergencykg.service;

import com.bysj.emergencykg.common.PageQuery;
import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.model.dto.KgDTO;
import com.bysj.emergencykg.model.vo.KgVO;

import java.util.List;
import java.util.Map;

public interface KnowledgeGraphService {
    List<KgVO.OntologyVO> listEntityTypes();
    List<KgVO.OntologyVO> listRelationTypes();
    void saveEntityType(KgDTO.OntologySaveDTO dto);
    void updateEntityType(Long id, KgDTO.OntologySaveDTO dto);
    void deleteEntityType(Long id);
    void saveRelationType(KgDTO.OntologySaveDTO dto);
    void updateRelationType(Long id, KgDTO.OntologySaveDTO dto);
    void deleteRelationType(Long id);
    PageResult<KgVO.EntityVO> pageEntities(KgDTO.EntityQueryDTO queryDTO);
    PageResult<KgVO.TripleVO> pageTriples(KgDTO.TripleQueryDTO queryDTO);
    KgVO.GraphVO graphView(KgDTO.GraphQueryDTO queryDTO);
    Map<String, Object> executeCypher(KgDTO.CypherQueryDTO queryDTO);
    KgVO.QaResponseVO askQuestion(KgDTO.QaRequestDTO requestDTO);
    KgVO.QaResponseVO reasoning(KgDTO.QaRequestDTO requestDTO);
    PageResult<KgVO.ConflictVO> pageConflicts(PageQuery pageQuery);
    void updateConflictStatus(Long id, Integer status);
    PageResult<KgVO.CompletionVO> pageCompletions(PageQuery pageQuery);
    void applyCompletion(Long id);
    PageResult<KgVO.VersionVO> pageVersions(PageQuery pageQuery);
    void createVersion(KgDTO.VersionSaveDTO dto);
    void syncGraphStoreSnapshot(String trigger);
}
