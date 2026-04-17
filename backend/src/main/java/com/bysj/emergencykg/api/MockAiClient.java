package com.bysj.emergencykg.api;

import org.springframework.stereotype.Component;

@Component
public class MockAiClient implements AiClient {
    @Override
    public ChatResponse chat(ChatRequest request) {
        ChatResponse response = new ChatResponse();
        response.setSuccess(true); response.setDurationMs(120); response.setRawBody("MOCK_RESPONSE"); response.setContent("【Mock响应】已基于当前知识图谱上下文完成处理：" + request.getMessage());
        return response;
    }
}
