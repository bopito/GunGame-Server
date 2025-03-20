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
                if (player != otherPlayer && isColliding(player, otherPlayer)) {
                    resolvePlayerCollision(player, otherPlayer);
                }
            }

            // 🔥 플레이어와 박스 충돌 검사 추가
            for (Box box : boxes) {
                if (isColliding(player, box)) {
                    resolvePlayerCollision(player, box);
                    System.out.println("[BLOCK] Player " + player.getId() + " collided with a box.");
                }
            }
        }
    }

    /**
     * Handles bullet collisions with players and walls.
     */
    private void checkBulletCollisions() {
        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) continue; // 비활성화된 총알은 검사하지 않음

            for (Player player : players) {
                // 🔥 자신이 쏜 총알은 무시
                if (bullet.getShooterId().equals(player.getId())) {
                    continue;
                }

                if (isColliding(bullet, player)) {
                    System.out.println("[HIT] Player " + player.getId() + " was hit by Bullet from " + bullet.getShooterId());
                    player.takeDamage(bullet.getDamage()); // 피해 적용
                    bullet.setActive(false); // 충돌한 총알은 삭제
                }
            }

            for (Box box : boxes) {
                if (isColliding(bullet, box)) {
                    System.out.println("[BULLET BLOCKED] Bullet hit a box and disappeared.");
                    bullet.setActive(false); // 박스와 충돌하면 총알 삭제
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
        double hitboxSizeA = (a instanceof Bullet) ? 0.5 : 1.0; // 총알 크기는 작게 설정
        double hitboxSizeB = (b instanceof Bullet) ? 0.5 : 1.0;

        boolean result = Math.abs(a.getX() - b.getX()) < (hitboxSizeA + hitboxSizeB) / 2 &&
                Math.abs(a.getZ() - b.getZ()) < (hitboxSizeA + hitboxSizeB) / 2;

        if (result) {
            System.out.println("[DEBUG] Collision detected between "
                    + a.getClass().getSimpleName() + " and " + b.getClass().getSimpleName());
        }

        return result;
    }


}
