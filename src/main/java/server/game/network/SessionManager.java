package server.game.network;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String playerId, WebSocketSession session) {
        sessions.put(playerId, session);
    }

    public void removeSession(String playerId) {
        sessions.remove(playerId);
    }

    public Collection<WebSocketSession> getSessions() {
        return sessions.values();
    }

    public WebSocketSession getSession(String playerId) {
        return sessions.get(playerId);
    }
}
