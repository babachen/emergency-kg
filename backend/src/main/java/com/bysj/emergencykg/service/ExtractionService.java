package com.bysj.emergencykg.service;

import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.model.dto.DocumentDTO;
import com.bysj.emergencykg.model.vo.DocumentVO;

public interface ExtractionService {
    PageResult<DocumentVO.TaskVO> pageTasks(DocumentDTO.TaskQueryDTO queryDTO);
    void saveTask(DocumentDTO.TaskSaveDTO dto);
    void executeTask(Long taskId);
}
