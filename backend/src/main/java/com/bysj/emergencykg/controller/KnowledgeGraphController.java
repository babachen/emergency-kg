package com.bysj.emergencykg.controller;

import com.bysj.emergencykg.annotation.RequirePermission;
import com.bysj.emergencykg.common.BaseResponse;
import com.bysj.emergencykg.common.PageQuery;
import com.bysj.emergencykg.common.ResultUtils;
import com.bysj.emergencykg.model.dto.KgDTO;
import com.bysj.emergencykg.model.vo.KgVO;
import com.bysj.emergencykg.service.KnowledgeGraphService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kg")
public class KnowledgeGraphController {
    private final KnowledgeGraphService knowledgeGraphService;
    public KnowledgeGraphController(KnowledgeGraphService knowledgeGraphService) { this.knowledgeGraphService = knowledgeGraphService; }
    @GetMapping("/entity-types") @RequirePermission("kg:ontology:view")
    public BaseResponse<List<KgVO.OntologyVO>> entityTypes() { return ResultUtils.success(knowledgeGraphService.listEntityTypes()); }
    @PostMapping("/entity-types") @RequirePermission("kg:ontology:edit")
    public BaseResponse<Void> saveEntityType(@Valid @RequestBody KgDTO.OntologySaveDTO dto) { knowledgeGraphService.saveEntityType(dto); return ResultUtils.success(); }
    @PutMapping("/entity-types/{id}") @RequirePermission("kg:ontology:edit")
    public BaseResponse<Void> updateEntityType(@PathVariable Long id, @Valid @RequestBody KgDTO.OntologySaveDTO dto) { knowledgeGraphService.updateEntityType(id, dto); return ResultUtils.success(); }
    @DeleteMapping("/entity-types/{id}") @RequirePermission("kg:ontology:edit")
    public BaseResponse<Void> deleteEntityType(@PathVariable Long id) { knowledgeGraphService.deleteEntityType(id); return ResultUtils.success(); }
    @GetMapping("/relation-types") @RequirePermission("kg:ontology:view")
    public BaseResponse<List<KgVO.OntologyVO>> relationTypes() { return ResultUtils.success(knowledgeGraphService.listRelationTypes()); }
    @PostMapping("/relation-types") @RequirePermission("kg:ontology:edit")
    public BaseResponse<Void> saveRelationType(@Valid @RequestBody KgDTO.OntologySaveDTO dto) { knowledgeGraphService.saveRelationType(dto); return ResultUtils.success(); }
    @PutMapping("/relation-types/{id}") @RequirePermission("kg:ontology:edit")
    public BaseResponse<Void> updateRelationType(@PathVariable Long id, @Valid @RequestBody KgDTO.OntologySaveDTO dto) { knowledgeGraphService.updateRelationType(id, dto); return ResultUtils.success(); }
    @DeleteMapping("/relation-types/{id}") @RequirePermission("kg:ontology:edit")
    public BaseResponse<Void> deleteRelationType(@PathVariable Long id) { knowledgeGraphService.deleteRelationType(id); return ResultUtils.success(); }
    @GetMapping("/entities") @RequirePermission("kg:entity:view")
    public BaseResponse<?> entities(KgDTO.EntityQueryDTO queryDTO) { return ResultUtils.success(knowledgeGraphService.pageEntities(queryDTO)); }
    @GetMapping("/triples") @RequirePermission("kg:triple:view")
    public BaseResponse<?> triples(KgDTO.TripleQueryDTO queryDTO) { return ResultUtils.success(knowledgeGraphService.pageTriples(queryDTO)); }
    @GetMapping("/graph") @RequirePermission("kg:graph:view")
    public BaseResponse<KgVO.GraphVO> graph(KgDTO.GraphQueryDTO queryDTO) { return ResultUtils.success(knowledgeGraphService.graphView(queryDTO)); }
    @PostMapping("/cypher") @RequirePermission("kg:graph:view")
    public BaseResponse<Map<String, Object>> cypher(@Valid @RequestBody KgDTO.CypherQueryDTO queryDTO) { return ResultUtils.success(knowledgeGraphService.executeCypher(queryDTO)); }
    @PostMapping("/qa") @RequirePermission("kg:qa:use")
    public BaseResponse<KgVO.QaResponseVO> qa(@Valid @RequestBody KgDTO.QaRequestDTO dto) { return ResultUtils.success(knowledgeGraphService.askQuestion(dto)); }
    @PostMapping("/reasoning") @RequirePermission("kg:reasoning:use")
    public BaseResponse<KgVO.QaResponseVO> reasoning(@Valid @RequestBody KgDTO.QaRequestDTO dto) { return ResultUtils.success(knowledgeGraphService.reasoning(dto)); }
    @GetMapping("/conflicts") @RequirePermission("kg:quality:view")
    public BaseResponse<?> conflicts(PageQuery query) { return ResultUtils.success(knowledgeGraphService.pageConflicts(query)); }
    @PutMapping("/conflicts/{id}/status") @RequirePermission("kg:quality:edit")
    public BaseResponse<Void> conflictStatus(@PathVariable Long id, @RequestParam Integer status) { knowledgeGraphService.updateConflictStatus(id, status); return ResultUtils.success(); }
    @GetMapping("/completions") @RequirePermission("kg:quality:view")
    public BaseResponse<?> completions(PageQuery query) { return ResultUtils.success(knowledgeGraphService.pageCompletions(query)); }
    @PostMapping("/completions/{id}/apply") @RequirePermission("kg:quality:edit")
    public BaseResponse<Void> apply(@PathVariable Long id) { knowledgeGraphService.applyCompletion(id); return ResultUtils.success(); }
    @GetMapping("/versions") @RequirePermission("kg:version:view")
    public BaseResponse<?> versions(PageQuery query) { return ResultUtils.success(knowledgeGraphService.pageVersions(query)); }
    @PostMapping("/versions") @RequirePermission("kg:version:add")
    public BaseResponse<Void> saveVersion(@Valid @RequestBody KgDTO.VersionSaveDTO dto) { knowledgeGraphService.createVersion(dto); return ResultUtils.success(); }
}
