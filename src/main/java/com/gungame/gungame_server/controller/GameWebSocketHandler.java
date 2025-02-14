package com.gungame.gungame_server.controller;

import com.gungame.gungame_server.game.GameEngine;
import com.gungame.gungame_server.game.PlayerSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final PlayerSessionManager sessionManager;
    private final GameEngine gameEngine;

    public GameWebSocketHandler(PlayerSessionManager sessionManager, GameEngine gameEngine) {
        this.sessionManager = sessionManager;
        this.gameEngine = gameEngine;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Log when a WebSocket connection is established
        System.out.println("WebSocket connection established: " + session.getId());
        
        // Add a new player to the session manager and broadcast the updated game state
        sessionManager.addPlayer(session);
        
        gameEngine.broadcastGameState();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // Update player state based on the received message
            sessionManager.updatePlayerState(session, message);
        } catch (Exception e) {
            // Log any exceptions to ensure the WebSocket session remains stable
            System.err.println("Error processing message from session: " + session.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Log when a WebSocket connection is closed
        System.out.println("WebSocket connection closed: " + session.getId() + " with status: " + status);

        // Remove the player from the session manager
        sessionManager.removePlayer(session);

        // Broadcast the updated game state after player removal
        try {
            gameEngine.broadcastGameState();
        } catch (Exception e) {
            // Log any exceptions during broadcasting
            System.err.println("Error broadcasting game state after closing session: " + session.getId());
            e.printStackTrace();
        }
    }
}
