package com.bysj.emergencykg.controller;

import com.bysj.emergencykg.annotation.RequirePermission;
import com.bysj.emergencykg.common.BaseResponse;
import com.bysj.emergencykg.common.ResultUtils;
import com.bysj.emergencykg.model.dto.DocumentDTO;
import com.bysj.emergencykg.service.ExtractionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/extraction")
public class ExtractionController {
    private final ExtractionService extractionService;
    public ExtractionController(ExtractionService extractionService) { this.extractionService = extractionService; }
    @GetMapping("/tasks") @RequirePermission("extract:task:view")
    public BaseResponse<?> tasks(DocumentDTO.TaskQueryDTO queryDTO) { return ResultUtils.success(extractionService.pageTasks(queryDTO)); }
    @PostMapping("/tasks") @RequirePermission("extract:task:add")
    public BaseResponse<Void> saveTask(@Valid @RequestBody DocumentDTO.TaskSaveDTO dto) { extractionService.saveTask(dto); return ResultUtils.success(); }
    @PostMapping("/tasks/{id}/execute") @RequirePermission("extract:task:run")
    public BaseResponse<Void> execute(@PathVariable Long id) { extractionService.executeTask(id); return ResultUtils.success(); }
}
