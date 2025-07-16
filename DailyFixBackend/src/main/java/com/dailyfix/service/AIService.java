package com.dailyfix.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@Service
public class AIService {

    @Value("${openai.api.key:}")
    private String openAIApiKey;

    private final WebClient webClient = WebClient.builder().baseUrl("https://api.openai.com/v1").build();

    public String getSummary(List<String> messages) {
        String joined = messages.stream().collect(Collectors.joining(". "));
        return summarizeText(joined);
    }

    public String summarizeText(String chatText) {
        if (openAIApiKey == null || openAIApiKey.isEmpty()) {
            // Fallback: return truncated text
            return "🧠 Summary: " + chatText.substring(0, Math.min(chatText.length(), 200)) + "...";
        }
        // Call OpenAI API (GPT-3.5 Turbo) for summarization
        String prompt = "Summarize the following chat conversation in a concise way:\n" + chatText;
        try {
            Mono<String> responseMono = webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + openAIApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                    "\"model\": \"gpt-3.5-turbo\"," +
                    "\"messages\": [{\"role\": \"user\", \"content\": " + "\"" + prompt.replace("\"", "\\\"") + "\"}]" +
                "}")
                .retrieve()
                .bodyToMono(String.class);
            String response = responseMono.block();
            // Extract summary from response (simple extraction)
            int contentIdx = response.indexOf("\"content\":");
            if (contentIdx != -1) {
                int start = response.indexOf('"', contentIdx + 11) + 1;
                int end = response.indexOf('"', start);
                if (start > 0 && end > start) {
                    return response.substring(start, end);
                }
            }
            return "[AI summary unavailable]";
        } catch (Exception e) {
            return "[AI error: " + e.getMessage() + "]";
        }
    }
}
