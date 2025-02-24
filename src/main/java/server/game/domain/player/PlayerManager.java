package server.game.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.stereotype.Component;
import server.game.network.SessionManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

@Component
public class PlayerManager {
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public Player getPlayerBySession(SessionManager sessionManager, WebSocketSession session) {
        return players.values().stream()
                .filter(p -> sessionManager.getSession(p.getId()) == session)
                .findFirst()
                .orElse(null);
    }

    public void updatePlayers() {
        for (Player player : players.values()) {
            player.update();
        }
    }

    public void updatePlayerState(Player player, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> incomingMessage = objectMapper.readValue(payload, Map.class);

        if (incomingMessage.containsKey("angle")) {
            player.setAngle(((Number) incomingMessage.get("angle")).doubleValue());
        }
        if (incomingMessage.containsKey("keys")) {
            // Modified to not use immutable objects (Map.of)
            Map<String, Boolean> updatedKeys = new HashMap<>(player.getKeys());
            updatedKeys.putAll((Map<String, Boolean>) incomingMessage.get("keys"));
            player.setKeys(updatedKeys);
        }
    }
}
