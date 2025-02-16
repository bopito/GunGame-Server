package server.network;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import server.core.GameEngine;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private final GameEngine gameEngine;

    public WebSocketHandler(SessionManager sessionManager, GameEngine gameEngine) {
        this.sessionManager = sessionManager;
        this.gameEngine = gameEngine;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocket connection established: " + session.getId());

        // Add a new player to the session manager
        sessionManager.addPlayer(session);

        // Broadcast updated game state
        sessionManager.broadcastGameState();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // Update player state based on received message
            sessionManager.updatePlayerState(session, message);
        } catch (Exception e) {
            System.err.println("Error processing message from session: " + session.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WebSocket connection closed: " + session.getId() + " with status: " + status);

        // Remove player from session manager
        sessionManager.removeSession(session);

        // Broadcast updated game state after player removal
        sessionManager.broadcastGameState();
    }
}
