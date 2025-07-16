package com.dailyfix.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class MatrixService {

    private final WebClient webClient;

    // Replace this with your actual Synapse homeserver URL
    private final String MATRIX_BASE_URL = "http://localhost:8008";

    public MatrixService() {
        this.webClient = WebClient.builder()
                .baseUrl(MATRIX_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    // 1. Login to Matrix (returns access token)
    public Mono<String> login(String username, String password) {
        return webClient.post()
                .uri("/_matrix/client/r0/login")
                .bodyValue(Map.of(
                        "type", "m.login.password",
                        "user", username,
                        "password", password
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> (String) response.get("access_token"));
    }

    // 2. Get joined rooms
    public Mono<Map<String, Object>> getJoinedRooms(String accessToken) {
        return webClient.get()
                .uri("/_matrix/client/r0/joined_rooms")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    // 3. Get messages for a specific room
    public Mono<Map<String, Object>> getMessages(String accessToken, String roomId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/_matrix/client/r0/rooms/{roomId}/messages")
                        .queryParam("dir", "b")
                        .queryParam("limit", "50")
                        .build(roomId))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    // 4. Send a message to a specific room
    /**
     * Sends a text message to a Matrix room.
     * @param accessToken Matrix access token
     * @param roomId Room ID to send the message to
     * @param message The message body
     * @return Matrix API response as a map
     */
    public Mono<Map<String, Object>> sendMessage(String accessToken, String roomId, String message) {
        return webClient.post()
                .uri("/_matrix/client/r0/rooms/" + roomId + "/send/m.room.message")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(Map.of(
                        "msgtype", "m.text",
                        "body", message
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
