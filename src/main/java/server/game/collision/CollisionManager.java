package server.game.collision;

import server.game.base.entity.Entity;
import server.game.domain.player.Player;
import server.game.domain.box.Box;
import server.game.domain.bullet.Bullet;
import java.util.List;

public class CollisionManager {
    private final List<Player> players;
    private final List<Box> boxes;
    private final List<Bullet> bullets;

    public CollisionManager(List<Player> players, List<Box> boxes, List<Bullet> bullets) {
        this.players = players;
        this.boxes = boxes;
        this.bullets = bullets;

        System.out.println("[DEBUG] CollisionManager created with:");
        System.out.println(" - Players: " + players.size());
        System.out.println(" - Boxes: " + boxes.size());
        System.out.println(" - Bullets: " + bullets.size());
    }

    /**
     * Updates collision detection for all entities every frame.
     */
    public void updateCollisions() {
        checkPlayerCollisions();  // Player vs Player, box
        checkBulletCollisions();  // Bullet vs Player, box
    }

    /**
     * Prevents players from passing through other players and walls.
     */
    private void checkPlayerCollisions() {
        for (Player player : players) {
            for (Player otherPlayer : players) {
                if (player != otherPlayer) {
                    if (isColliding(player, otherPlayer)) {
                        resolvePlayerCollision(player, otherPlayer);
                    }
                }
            }

            for (Box box : boxes) {
                if (isColliding(player, box)) {
                    resolvePlayerCollision(player, box);
                }
            }
        }
    }


    /**
     * Handles bullet collisions with players and walls.
     */
    private void checkBulletCollisions() {
        for (Bullet bullet : bullets) {
            for (Player player : players) {
                if (isColliding(bullet, player)) {
                    player.takeDamage(bullet.getDamage());
                    bullet.setActive(false); // Destroy bullet on impact
                }
            }

            for (Box box : boxes) {
                if (isColliding(bullet, box)) {
                    bullet.setActive(false); // Destroy bullet on impact
                }
            }
        }
    }

    /**
     * Resolves player collision by preventing movement.
     */
    private void resolvePlayerCollision(Player player, Entity entity) {
        System.out.println("[Collision] Player " + player.getId() + " is blocked by " + entity.getClass().getSimpleName());
    }

    /**
     * Checks if two entities are colliding (AABB - Axis-Aligned Bounding Box).
     */
    private boolean isColliding(Entity a, Entity b) {
        double hitboxSize = 1.0;
        boolean result = Math.abs(a.getX() - b.getX()) < hitboxSize &&
                Math.abs(a.getZ() - b.getZ()) < hitboxSize;

        if (result) {
            System.out.println("[DEBUG] Collision detected between "
                    + a.getClass().getSimpleName() + " and " + b.getClass().getSimpleName());
        }

        return result;
    }

}
