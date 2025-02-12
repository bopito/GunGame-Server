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
        sessionManager.addPlayer(session);
        gameEngine.broadcastGameState();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        sessionManager.updatePlayerState(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.removePlayer(session);
        gameEngine.broadcastGameState();
    }
}
