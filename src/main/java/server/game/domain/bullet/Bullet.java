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
     * Updates the bullet's position and range check.
     */
    public void update() {
        if (!isActive) return;

        // Calculate movement distance
        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians) * speed;
        double dz = Math.sin(radians) * speed;

        this.x += dx;
        this.z += dz;
        traveledDistance += Math.sqrt(dx * dx + dz * dz);

        // Disable bullet if it exceeds its range
        if (traveledDistance >= range) {
            isActive = false;
            System.out.println("[Bullet] Bullet reached max range and disappeared.");
        }
    }
}
