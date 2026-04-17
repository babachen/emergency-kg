package com.bysj.emergencykg.controller;

import com.bysj.emergencykg.annotation.RequirePermission;
import com.bysj.emergencykg.common.BaseResponse;
import com.bysj.emergencykg.common.ResultUtils;
import com.bysj.emergencykg.model.dto.DocumentDTO;
import com.bysj.emergencykg.model.entity.Region;
import com.bysj.emergencykg.model.vo.DocumentVO;
import com.bysj.emergencykg.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentController {
    private final DocumentService documentService;
    public DocumentController(DocumentService documentService) { this.documentService = documentService; }
    @GetMapping("/regions")
    public BaseResponse<List<Region>> regions() { return ResultUtils.success(documentService.listRegions()); }
    @GetMapping("/documents") @RequirePermission("document:view")
    public BaseResponse<?> documents(DocumentDTO.DocumentQueryDTO queryDTO) { return ResultUtils.success(documentService.pageDocuments(queryDTO)); }
    @GetMapping("/documents/{id}") @RequirePermission("document:view")
    public BaseResponse<DocumentVO.DocumentItemVO> detail(@PathVariable Long id) { return ResultUtils.success(documentService.getDocument(id)); }
    @PostMapping("/documents") @RequirePermission("document:add")
    public BaseResponse<Void> save(@Valid @RequestBody DocumentDTO.DocumentSaveDTO dto) { documentService.saveDocument(dto); return ResultUtils.success(); }
    @PutMapping("/documents/{id}") @RequirePermission("document:edit")
    public BaseResponse<Void> update(@PathVariable Long id, @Valid @RequestBody DocumentDTO.DocumentSaveDTO dto) { documentService.updateDocument(id, dto); return ResultUtils.success(); }
    @DeleteMapping("/documents/{id}") @RequirePermission("document:delete")
    public BaseResponse<Void> delete(@PathVariable Long id) { documentService.deleteDocument(id); return ResultUtils.success(); }
    @PostMapping("/documents/{id}/preprocess") @RequirePermission("document:preprocess")
    public BaseResponse<Integer> preprocess(@PathVariable Long id) { return ResultUtils.success(documentService.preprocess(id)); }
    @GetMapping("/documents/{id}/sections") @RequirePermission("document:view")
    public BaseResponse<List<DocumentVO.SectionVO>> sections(@PathVariable Long id) { return ResultUtils.success(documentService.listSections(id)); }
}
