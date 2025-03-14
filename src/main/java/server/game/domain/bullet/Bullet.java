package server.game.domain.bullet;

import lombok.Getter;
import server.game.base.projectile.Projectile;

@Getter
public class Bullet extends Projectile {
    private boolean isActive = true; // Indicates whether the bullet is active
    private double traveledDistance = 0; // Tracks the distance traveled by the bullet

    public Bullet(double x, double y, double z, double angle, double speed, int damage, String shooterId, double range) {
        super(x, y, z, angle, speed, damage, shooterId, range);
    }

    /**
     * Updates the bullet's position and checks for collision or range limit.
     */
    public void update() {
        if (!isActive) return;

        // Calculate movement distance
        double dx = Math.cos(angle) * speed;
        double dy = Math.sin(angle) * speed;

        this.x += dx;
        this.y += dy;
        traveledDistance += Math.sqrt(dx * dx + dy * dy);

        // Check for collision (e.g., player, box)
        if (checkCollision()) {
            isActive = false;
            System.out.println("[Bullet] Bullet hit an object and is now inactive.");
            return;
        }

        // Disable bullet if it exceeds its range
        if (traveledDistance >= range) {
            isActive = false;
            System.out.println("[Bullet] Bullet reached max range and disappeared.");
        }
    }

    /**
     * Checks if the bullet collides with an object.
     */
    private boolean checkCollision() {
        // Collision detection logic (to be implemented)
        return false;
    }

    public boolean isActive() {
        return isActive;
    }
}
