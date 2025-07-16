package com.dailyfix.controller;

import com.dailyfix.service.MatrixService;
import com.dailyfix.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*") // Allow frontend to access backend
public class AIController {

    @Autowired
    private MatrixService matrixService;
    @Autowired
    private AIService aiService;

    @PostMapping("/summarize")
    public Map<String, Object> summarize(@RequestBody Map<String, String> request) {
        String roomId = request.get("roomId");
        String chatText = request.get("chatText");
        String accessToken = request.get("accessToken");
        String summary;
        if (roomId != null && accessToken != null) {
            // Fetch messages from Matrix
            try {
                Mono<Map<String, Object>> messagesMono = matrixService.getMessages(accessToken, roomId);
                Map<String, Object> messagesResp = messagesMono.block();
                List<String> messages = MatrixMessageExtractor.extractMessages(messagesResp);
                summary = aiService.getSummary(messages);
            } catch (Exception e) {
                summary = "[Matrix fetch error: " + e.getMessage() + "]";
            }
        } else if (chatText != null) {
            summary = aiService.summarizeText(chatText);
        } else {
            summary = "[No input provided]";
        }
        return Map.of("summary", summary);
    }

    // Helper to extract message texts from Matrix response
    static class MatrixMessageExtractor {
        @SuppressWarnings("unchecked")
        public static List<String> extractMessages(Map<String, Object> messagesResp) {
            // Matrix response: {chunk: [{content: {body: ...}, ...}, ...]}
            return ((List<Object>) messagesResp.getOrDefault("chunk", List.of())).stream()
                .map(obj -> {
                    if (obj instanceof Map<?, ?> event) {
                        Object content = event.get("content");
                        if (content instanceof Map<?, ?> contentMap) {
                            Object body = contentMap.get("body");
                            if (body != null) return body.toString();
                        }
                    }
                    return null;
                })
                .filter(s -> s != null && !s.isEmpty())
                .map(Object::toString)
                .toList();
        }
    }
}
