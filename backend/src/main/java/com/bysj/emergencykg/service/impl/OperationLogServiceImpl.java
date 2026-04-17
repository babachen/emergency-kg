package com.bysj.emergencykg.service.impl;

import com.bysj.emergencykg.context.UserContext;
import com.bysj.emergencykg.mapper.SysOperationLogMapper;
import com.bysj.emergencykg.model.entity.SysOperationLog;
import com.bysj.emergencykg.service.OperationLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    private final SysOperationLogMapper logMapper;

    public OperationLogServiceImpl(SysOperationLogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public void log(String moduleName, String operationType, String content) {
        UserContext.UserSession session = UserContext.get();
        SysOperationLog log = new SysOperationLog();
        log.setModuleName(moduleName);
        log.setOperationType(operationType);
        log.setContent(content);
        log.setOperatorId(session == null ? null : session.getUserId());
        log.setOperatorName(session == null ? "系统" : session.getRealName());
        log.setIpAddress("127.0.0.1");
        log.setCreateTime(LocalDateTime.now());
        logMapper.insert(log);
    }
}
