package com.bysj.emergencykg.controller;

import com.bysj.emergencykg.annotation.RequirePermission;
import com.bysj.emergencykg.common.BaseResponse;
import com.bysj.emergencykg.common.ResultUtils;
import com.bysj.emergencykg.model.vo.DocumentVO;
import com.bysj.emergencykg.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;
    public FileController(FileService fileService) { this.fileService = fileService; }
    @PostMapping("/upload") @RequirePermission("document:upload")
    public BaseResponse<DocumentVO.FileVO> upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "businessType", required = false) String businessType) { return ResultUtils.success(fileService.upload(file, businessType)); }
}
