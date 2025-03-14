package server.game.core;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import server.game.domain.box.Box;
import server.game.domain.box.BoxManager;
import server.game.domain.box.BoxSpawner;
import server.game.domain.bullet.Bullet;
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
    private final BoxSpawner boxSpawner;

    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduledExecutorService gameLoopExecutor = Executors.newSingleThreadScheduledExecutor();
    private boolean isSpawningEnabled = false; // ✅ Track if spawning should start


    // Event queue for handling weapon pickup and shooting requests
    private final Queue<Runnable> eventQueue = new ConcurrentLinkedQueue<>();
    // List to track bullets
    private final List<Bullet> bullets = new CopyOnWriteArrayList<>();

    @Autowired
    public GameEngine(PlayerManager playerManager, WeaponManager weaponManager, SessionManager sessionManager, BoxSpawner boxSpawner) {
        this.playerManager = playerManager;
        this.weaponManager = weaponManager;
        this.sessionManager = sessionManager;
        this.boxSpawner = boxSpawner;

        // add BoxSpawner listener
        this.boxSpawner.setBoxSpawnListener(this::broadcastBoxSpawn);

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
        updateBullets();
        broadcastGameState();
    }

    /**
     * Updates bullet positions and removes bullets that exceed range or collide.
     */
    private void updateBullets() {
        bullets.removeIf(bullet -> {
            bullet.update();
            return !bullet.isActive(); // Remove bullets that are inactive (collided or out of range)
        });
    }


    //broadcast player status&weapon
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

    //broadcast box
    private void broadcastBoxSpawn(Box box) {
        try {
            Map<String, Object> message = Map.of(
                    "type", "box_spawn",
                    "x", box.getX(),
                    "z", box.getZ(),
                    "weapon", box.getDroppedWeapon(),
                    "hp", box.getHp()
            );
            String jsonMessage = objectMapper.writeValueAsString(message);

            for (WebSocketSession session : sessionManager.getSessions()) {
                if (session.isOpen()) {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(jsonMessage));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[GameEngine] Error broadcasting box spawn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Broadcasts a new bullet spawn event to all clients.
     */
    private void broadcastBulletSpawn(Bullet bullet) {
        try {
            Map<String, Object> bulletData = Map.of(
                    "type", "bullet_spawn",
                    "playerId", bullet.getShooterId(),
                    "x", bullet.getX(),
                    "y", bullet.getY(),
                    "z", bullet.getZ(),
                    "angle", bullet.getAngle(),
                    "speed", bullet.getSpeed(),
                    "range", bullet.getRange()
            );
            String jsonMessage = objectMapper.writeValueAsString(bulletData);

            for (WebSocketSession session : sessionManager.getSessions()) {
                if (session.isOpen()) {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(jsonMessage));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[GameEngine] Error broadcasting bullet spawn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Called when a new player joins the game.
     */
    public void onPlayerJoined() {
        if (!isSpawningEnabled) { // ✅ Start spawning only once
            isSpawningEnabled = true;
            boxSpawner.startSpawning(); // ✅ Start spawning when the first player connects
            System.out.println("[GameEngine] First player joined, starting box spawning...");
        }
    }

    /**
     * Called when a player leaves the game.
     */
    public void onPlayerLeft() {
        if (sessionManager.getSessions().isEmpty()) { // ✅ Stop spawning when no players are online
            isSpawningEnabled = false;
            boxSpawner.stopSpawning(); // ✅ Stop spawning if no players remain
            System.out.println("[GameEngine] No players left, stopping box spawning...");
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
    /**
     * Queues a player shooting action and adds the bullet to the game.
     */
    public void queuePlayerShoot(Player player) {
        eventQueue.add(() -> {
            Bullet bullet = player.shootBullet(); // Get the bullet instance
            if (bullet != null) {
                bullets.add(bullet); // Add to active bullets
                broadcastBulletSpawn(bullet); // Notify clients
            }
        });
    }


    public void queuePlayerReload(Player player) {
        eventQueue.add(player::reloadBullet);
    }

}
