package com.gungame.gungame_server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gungame.gungame_server.entity.Player;
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
    private final Map<String, Player> playerStates = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameWebSocketHandler() {
        startGameLoop();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Player connected: " + session.getId());

        // Ensure no duplicate players exist
        if (playerStates.containsKey(session.getId())) {
            System.out.println("Removing old session: " + session.getId());
            removePlayer(session.getId());
        }

        players.put(session.getId(), session);

        // Create a new Player object in memory
        int team = (int) (Math.random() * 2) + 1;
        Player newPlayer = new Player(team);
        playerStates.put(session.getId(), newPlayer);

        broadcastGameState();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> incomingMessage = objectMapper.readValue(payload, Map.class);

        Player player = playerStates.get(session.getId());
        if (player == null) return;

        if (incomingMessage.containsKey("keys")) {
            player.setKeys((Map<String, Boolean>) incomingMessage.get("keys"));
        }

        if (incomingMessage.containsKey("angle")) {
            player.setAngle((double) incomingMessage.get("angle"));
        }
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30); // ~33 FPS

                    for (Player player : playerStates.values()) {
                        updatePlayerPosition(player);
                    }

                    // Remove Disconnected Players
                    players.entrySet().removeIf(entry -> {
                        if (!entry.getValue().isOpen()) {
                            removePlayer(entry.getKey());
                            return true;
                        }
                        return false;
                    });

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

    private void broadcastGameState() {
        try {
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
        removePlayer(session.getId());
    }

    private void removePlayer(String playerId) {
        players.remove(playerId);
        playerStates.remove(playerId);
        broadcastGameState();
    }
}
