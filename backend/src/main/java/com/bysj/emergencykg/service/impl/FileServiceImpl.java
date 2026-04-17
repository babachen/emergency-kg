package com.bysj.emergencykg.service.impl;

import com.bysj.emergencykg.config.AppProperties;
import com.bysj.emergencykg.context.UserContext;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.mapper.FileStorageMapper;
import com.bysj.emergencykg.model.entity.FileStorage;
import com.bysj.emergencykg.model.vo.DocumentVO;
import com.bysj.emergencykg.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final AppProperties appProperties;
    private final FileStorageMapper fileStorageMapper;

    public FileServiceImpl(AppProperties appProperties, FileStorageMapper fileStorageMapper) {
        this.appProperties = appProperties;
        this.fileStorageMapper = fileStorageMapper;
    }

    @Override
    public DocumentVO.FileVO upload(MultipartFile file, String businessType) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        try {
            String suffix = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String folder = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String storedName = UUID.randomUUID().toString().replace("-", "") + (StringUtils.hasText(suffix) ? "." + suffix : "");
            Path saveDir = Path.of(appProperties.getStorage().getLocation(), folder);
            Files.createDirectories(saveDir);
            Path savePath = saveDir.resolve(storedName);
            file.transferTo(savePath.toFile());
            String relative = folder + "/" + storedName;
            FileStorage storage = new FileStorage();
            storage.setBusinessType(StringUtils.hasText(businessType) ? businessType : "common");
            storage.setOriginalName(file.getOriginalFilename());
            storage.setStoredName(storedName);
            storage.setFilePath(savePath.toString());
            storage.setFileUrl(appProperties.getStorage().getAccessPath() + "/" + relative);
            storage.setFileSize(file.getSize());
            storage.setContentType(file.getContentType());
            storage.setUploaderId(UserContext.getUserId());
            storage.setCreateTime(LocalDateTime.now());
            fileStorageMapper.insert(storage);
            DocumentVO.FileVO vo = new DocumentVO.FileVO();
            vo.setId(storage.getId());
            vo.setOriginalName(storage.getOriginalName());
            vo.setFilePath(storage.getFilePath());
            vo.setFileUrl(storage.getFileUrl());
            vo.setFileSize(storage.getFileSize());
            return vo;
        } catch (IOException e) {
            throw new BusinessException("文件保存失败：" + e.getMessage());
        }
    }
}
