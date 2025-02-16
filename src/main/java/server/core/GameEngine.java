package server.core;

import org.springframework.stereotype.Component;
import server.entity.EntityManager;
import server.network.SessionManager;

@Component
public class GameEngine {
    private final EntityManager entityManager;
    private final SessionManager sessionManager;

    public GameEngine(EntityManager entityManager, SessionManager sessionManager) {
        this.entityManager = entityManager;
        // players
        // itmes
        // projectiles
        this.sessionManager = sessionManager;
        startGameLoop();
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30);
                    entityManager.updateEntities(); // Now updates movements & collisions
                    broadcastGameState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void broadcastGameState() {
        sessionManager.broadcastGameState();
    }
}
