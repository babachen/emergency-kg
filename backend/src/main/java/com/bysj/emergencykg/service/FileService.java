package com.bysj.emergencykg.service;

import com.bysj.emergencykg.model.vo.DocumentVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    DocumentVO.FileVO upload(MultipartFile file, String businessType);
}
