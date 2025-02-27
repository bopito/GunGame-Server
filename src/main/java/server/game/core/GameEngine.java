package server.game.core;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import server.game.domain.player.Player;
import server.game.domain.player.PlayerManager;
import server.game.domain.weapon.Weapon;
import server.game.domain.weapon.WeaponManager;
import server.game.network.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GameEngine {
    private final PlayerManager playerManager;
    private final WeaponManager weaponManager;
    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduledExecutorService gameLoopExecutor = Executors.newSingleThreadScheduledExecutor();

    // Event queue for handling weapon pickup and shooting requests
    private final Queue<Runnable> eventQueue = new ConcurrentLinkedQueue<>();

    public GameEngine(PlayerManager playerManager, WeaponManager weaponManager, SessionManager sessionManager) {
        this.playerManager = playerManager;
        this.weaponManager = weaponManager;
        this.sessionManager = sessionManager;
        startGameLoop();
    }

    private void startGameLoop() {
        gameLoopExecutor.scheduleAtFixedRate(() -> {
            try {
                processEvents(); // Process queued events
                updateGame();
            } catch (Exception e) {
                System.err.println("[GameLoop Error] " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.MILLISECONDS);
    }

    /**
     * Process all events in the queue.
     */
    private void processEvents() {
        while (!eventQueue.isEmpty()) {
            eventQueue.poll().run();
        }
    }

    private void updateGame() {
        playerManager.updatePlayers();
        broadcastGameState();
    }

    private void broadcastGameState() {
        try {
            Map<String, Object> entities = Map.of(
                    "players", playerManager.getPlayers()
            );

            Map<String, Object> gameState = Map.of(
                    "type", "broadcast",
                    "entities", entities);
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

    /**
     * Queue a weapon pickup request.
     */
    public void queueWeaponPickup(Player player, String weaponId) {
        eventQueue.add(() -> {
            Weapon weapon = weaponManager.getWeapon(weaponId);
            if (weapon != null) {
                player.equipWeapon(weapon);
                weaponManager.removeDroppedWeapon(weapon);
                System.out.println("[GameEngine] " + player.getId() + " picked up " + weapon.getName());
            }
        });
    }

    /**
     * Queue a player shooting request.
     */
    public void queuePlayerShoot(Player player) {
        eventQueue.add(player::shootBullet);
    }

}
