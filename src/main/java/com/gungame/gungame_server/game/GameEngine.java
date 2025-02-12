package com.gungame.gungame_server.game;/*
 * created by seokhyun on 2025-02-13.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gungame.gungame_server.entity.Player;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class GameEngine {

    private final PlayerSessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameEngine(PlayerSessionManager sessionManager) {
        this.sessionManager = sessionManager;
        startGameLoop();
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30);
                    for (Player player : sessionManager.getPlayerStates().values()) {
                        updatePlayerPosition(player);
                    }
                    broadcastGameState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updatePlayerPosition(Player player) {
        int speed = 5;
        Map<String, Boolean> keys = player.getKeys();

        if (keys.getOrDefault("w", false)) player.setY(player.getY() - speed);
        if (keys.getOrDefault("s", false)) player.setY(player.getY() + speed);
        if (keys.getOrDefault("a", false)) player.setX(player.getX() - speed);
        if (keys.getOrDefault("d", false)) player.setX(player.getX() + speed);
    }

    public void broadcastGameState() {
        try {
            Map<String, Object> gameState = Map.of("players", sessionManager.getPlayerStates());
            String gameStateJson = objectMapper.writeValueAsString(gameState);

            for (WebSocketSession session : sessionManager.getSessions()) {
                if (session.isOpen()) {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(gameStateJson));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
