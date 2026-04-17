package com.bysj.emergencykg.api;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AiClient {
    ChatResponse chat(ChatRequest request);
    @Data
    class ChatRequest {
        private String projectId;
        private String message;
        private List<Map<String, Object>> history = new ArrayList<>();
        private boolean stream = false;
    }
    @Data
    class ChatResponse {
        private boolean success;
        private String content;
        private String rawBody;
        private String errorMessage;
        private long durationMs;
    }
}
