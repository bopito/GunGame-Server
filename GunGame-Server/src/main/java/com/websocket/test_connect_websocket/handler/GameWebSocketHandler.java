package com.websocket.test_connect_websocket.handler;/*
 * created by seokhyun on 2025-02-12.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> players = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Integer>> playerStates = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession client) {
        System.out.println("Player connected: " + client.getId());
        players.put(client.getId(), client);
        System.out.println(players);
        // 초기 플레이어 상태 설정
        playerStates.put(client.getId(), Map.of("x", 100, "y", 100));
        System.out.println(playerStates);
        broadcastGameState();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> incomingMessage = objectMapper.readValue(payload, Map.class);

        // 움직임(Action) 메시지 처리
        if (incomingMessage.containsKey("direction")) {
            handleActionMessage(session.getId(), incomingMessage);
        }
    }

    private void handleActionMessage(String playerId, Map<String, Object> message) {
        String direction = (String) message.get("direction");

        Map<String, Integer> currentState = playerStates.get(playerId);
        int x = currentState.get("x");
        int y = currentState.get("y");

        // 방향에 따라 플레이어 위치 업데이트
        switch (direction) {
            case "right":
                x += 10;
                break;
            case "left":
                x -= 10;
                break;
            case "up":
                y -= 10;
                break;
            case "down":
                y += 10;
                break;
        }

        playerStates.put(playerId, Map.of("x", x, "y", y));
        broadcastGameState();
    }

    private void broadcastGameState() {
        try {
            // 현재 상태를 모든 클라이언트에 브로드캐스트
            Map<String, Object> gameState = Map.of("players", playerStates);
            String gameStateJson = objectMapper.writeValueAsString(gameState);

            for (WebSocketSession session : players.values()) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(gameStateJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Player disconnected: " + session.getId());
        players.remove(session.getId());
        playerStates.remove(session.getId());
        broadcastGameState();
    }
}