package com.bysj.emergencykg.service;

public interface OperationLogService {
    void log(String moduleName, String operationType, String content);
}
