package com.dailyfix.controller;

import com.dailyfix.model.SummaryRequest;
import com.dailyfix.service.AIService;
import com.dailyfix.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private AIService aiService;
    @Autowired
    private MatrixService matrixService;

    @PostMapping("/generate-summary")
    public String generateSummary(@RequestBody SummaryRequest request) {
        return aiService.getSummary(request.getMessages());
    }

    @PostMapping("/send-message")
    public Map<String, Object> sendMessage(@RequestBody Map<String, String> request) {
        String accessToken = request.get("accessToken");
        String roomId = request.get("roomId");
        String message = request.get("message");
        if (accessToken == null || roomId == null || message == null) {
            return Map.of("error", "Missing required fields");
        }
        return matrixService.sendMessage(accessToken, roomId, message).block();
    }
}
