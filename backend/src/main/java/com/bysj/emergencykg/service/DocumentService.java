package com.bysj.emergencykg.service;

import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.model.dto.DocumentDTO;
import com.bysj.emergencykg.model.entity.Region;
import com.bysj.emergencykg.model.vo.DocumentVO;

import java.util.List;

public interface DocumentService {
    List<Region> listRegions();
    PageResult<DocumentVO.DocumentItemVO> pageDocuments(DocumentDTO.DocumentQueryDTO queryDTO);
    DocumentVO.DocumentItemVO getDocument(Long id);
    void saveDocument(DocumentDTO.DocumentSaveDTO dto);
    void updateDocument(Long id, DocumentDTO.DocumentSaveDTO dto);
    void deleteDocument(Long id);
    List<DocumentVO.SectionVO> listSections(Long documentId);
    int preprocess(Long documentId);
}
