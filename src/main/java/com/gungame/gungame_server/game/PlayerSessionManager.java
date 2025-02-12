package com.gungame.gungame_server.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gungame.gungame_server.entity.Player;
import com.gungame.gungame_server.service.PlayerService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerSessionManager {

    private final Map<String, WebSocketSession> players = new ConcurrentHashMap<>();
    private final Map<String, Player> playerStates = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PlayerService playerService;

    public PlayerSessionManager(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void addPlayer(WebSocketSession session) {
        int team = (int) (Math.random() * 2) + 1;
        Player newPlayer = new Player(team);

        players.put(session.getId(), session);
        playerStates.put(session.getId(), newPlayer);
    }

    public void updatePlayerState(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> incomingMessage = objectMapper.readValue(payload, Map.class);

        Player player = playerStates.get(session.getId());
        if (player != null) {
            playerService.updatePlayerState(player, incomingMessage);
        }
    }

    public void removePlayer(WebSocketSession session) {
        players.remove(session.getId());
        playerStates.remove(session.getId());
    }

    public Map<String, Player> getPlayerStates() {
        return playerStates;
    }

    public Collection<WebSocketSession> getSessions() {
        return players.values();
    }
}
