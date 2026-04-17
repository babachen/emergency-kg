package com.bysj.emergencykg.service.impl;

import com.bysj.emergencykg.api.AiClient;
import com.bysj.emergencykg.api.BysjChatAiClient;
import com.bysj.emergencykg.api.MockAiClient;
import com.bysj.emergencykg.config.AppProperties;
import com.bysj.emergencykg.mapper.AiCallRecordMapper;
import com.bysj.emergencykg.model.entity.AiCallRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class AiSupportService {
    private final AppProperties appProperties;
    private final BysjChatAiClient bysjChatAiClient;
    private final MockAiClient mockAiClient;
    private final AiCallRecordMapper aiCallRecordMapper;

    public AiSupportService(AppProperties appProperties, BysjChatAiClient bysjChatAiClient, MockAiClient mockAiClient, AiCallRecordMapper aiCallRecordMapper) {
        this.appProperties = appProperties;
        this.bysjChatAiClient = bysjChatAiClient;
        this.mockAiClient = mockAiClient;
        this.aiCallRecordMapper = aiCallRecordMapper;
    }

    public String chat(String bizType, Long bizId, String prompt) {
        AiClient client = useBysj() ? bysjChatAiClient : mockAiClient;
        AiClient.ChatRequest request = new AiClient.ChatRequest();
        request.setProjectId(appProperties.getAi().getProjectId());
        request.setMessage(prompt);
        AiClient.ChatResponse response = client.chat(request);
        AiCallRecord record = new AiCallRecord();
        record.setBizType(bizType);
        record.setBizId(bizId);
        record.setProviderName(useBysj() ? "bysj-chat-api" : "mock");
        record.setProjectId(appProperties.getAi().getProjectId());
        record.setRequestPrompt(prompt);
        record.setResponseText(response.getContent());
        record.setCallStatus(response.isSuccess() ? 1 : 0);
        record.setDurationMs(response.getDurationMs());
        record.setErrorMessage(response.getErrorMessage());
        record.setCreateTime(LocalDateTime.now());
        aiCallRecordMapper.insert(record);
        return StringUtils.hasText(response.getContent()) ? response.getContent() : "";
    }

    public boolean useBysj() {
        return "bysj".equalsIgnoreCase(appProperties.getAi().getProvider()) && !Boolean.TRUE.equals(appProperties.getAi().getMockEnabled());
    }
}
