package com.bysj.emergencykg.api;

import com.bysj.emergencykg.config.AppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BysjChatAiClient implements AiClient {
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public BysjChatAiClient(RestTemplate restTemplate, AppProperties appProperties) { this.restTemplate = restTemplate; this.appProperties = appProperties; }
    @Override
    public ChatResponse chat(ChatRequest request) {
        long start = System.currentTimeMillis();
        ChatResponse response = new ChatResponse();
        try {
            String url = appProperties.getAi().getBaseUrl() + "/api/chat";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> body = new HashMap<>();
            body.put("projectId", request.getProjectId()); body.put("message", request.getMessage()); body.put("history", request.getHistory()); body.put("stream", request.isStream());
            ResponseEntity<String> entity = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
            response.setRawBody(entity.getBody()); response.setContent(extractText(entity.getBody())); response.setSuccess(true);
        } catch (Exception e) {
            log.warn("调用 bysj-chat-api 失败：{}", e.getMessage());
            response.setSuccess(false); response.setErrorMessage(e.getMessage());
        }
        response.setDurationMs(System.currentTimeMillis() - start);
        return response;
    }
    private String extractText(String body) {
        if (body == null || body.isBlank()) { return ""; }
        try {
            JsonNode node = objectMapper.readTree(body);
            if (node.has("content")) { return node.get("content").asText(); }
            if (node.has("message")) { return node.get("message").asText(); }
            if (node.has("data")) {
                JsonNode dataNode = node.get("data");
                if (dataNode.has("content")) { return dataNode.get("content").asText(); }
                if (dataNode.has("message")) { return dataNode.get("message").asText(); }
                if (dataNode.has("reply")) { return dataNode.get("reply").asText(); }
            }
        } catch (Exception ignore) { return body; }
        return body;
    }
}
