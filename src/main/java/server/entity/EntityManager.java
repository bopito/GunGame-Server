package server.entity;

import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EntityManager {
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Map<String, Projectile> projectiles = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public void addProjectile(Projectile projectile) {
        projectiles.put(projectile.getId(), projectile);
    }

    public void removeProjectile(String projectileId) {
        projectiles.remove(projectileId);
    }

    public Collection<Player> getPlayers() { return players.values(); }
    public Collection<Projectile> getProjectiles() { return projectiles.values(); }

    //
    // Updates all entities and checks for collisions.
    //
    public void updateEntities() {
        updateEntityStates(); // Move all entities
        checkCollisions();    // Handle entity interactions
    }

    //
    // Update all players and projectiles
    //
    private void updateEntityStates() {
        players.values().forEach(Player::update);
        projectiles.values().forEach(Projectile::update);
    }

    //
    // Detects and handle collisions between entities
    //
    private void checkCollisions() {
        Iterator<Projectile> projectileIterator = projectiles.values().iterator();

        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();

            for (Player player : players.values()) {
                if (!player.getId().equals(projectile.getShooterId()) && isColliding(player, projectile)) {
                    System.out.println("Player " + player.getId() + " hit by projectile!");
                    projectileIterator.remove(); // Remove projectile on hit
                }
            }
        }
    }

    //
    // Updates player state based on client input.
    //
    public void updatePlayerState(Player player, Map<String, Object> incomingMessage) {
        if (incomingMessage.containsKey("x")) {
            player.setX(((Number) incomingMessage.get("x")).intValue());
        }
        if (incomingMessage.containsKey("y")) {
            player.setY(((Number) incomingMessage.get("y")).intValue());
        }
        if (incomingMessage.containsKey("angle")) {
            player.setAngle(((Number) incomingMessage.get("angle")).doubleValue());
        }
        if (incomingMessage.containsKey("keys")) {
            player.setKeys((Map<String, Boolean>) incomingMessage.get("keys"));
        }
    }

    //
    // Checks if a projectile has hit a player.
    //
    private boolean isColliding(Player player, Projectile projectile) {
        return Math.abs(player.getX() - projectile.getX()) < 10 &&
               Math.abs(player.getY() - projectile.getY()) < 10;
    }
}
