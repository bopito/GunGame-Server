package server.game.core;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import server.game.domain.player.PlayerManager;
import server.game.network.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GameEngine {
    private final PlayerManager playerManager;
    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduledExecutorService gameLoopExecutor = Executors.newSingleThreadScheduledExecutor();

    public GameEngine(PlayerManager playerManager, SessionManager sessionManager) {
        this.playerManager = playerManager;
        this.sessionManager = sessionManager;
        startGameLoop();
    }

    private void startGameLoop() {
        gameLoopExecutor.scheduleAtFixedRate(() -> {
            try {
                updateGame();
            } catch (Exception e) {
                System.err.println("[GameLoop Error] " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.MILLISECONDS);
    }

    private void updateGame() {
        playerManager.updatePlayers();
        broadcastGameState();
    }

    private void broadcastGameState() {
        try {
            Map<String, Object> entities = Map.of(
                    "players", playerManager.getPlayers()
//                    "projectiles", projectiles.getProjectiles()
                    //"items", entityManager.getItems(),
                    //"walls", entityManager.getWalls()
            );

            Map<String, Object> gameState = Map.of("entities", entities);
            String gameStateJson = objectMapper.writeValueAsString(gameState);


            for (WebSocketSession session : sessionManager.getSessions()) {
                if (session.isOpen()) {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(gameStateJson));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Broadcast Error] " + e.getMessage());
            e.printStackTrace();
        }
    }

}
