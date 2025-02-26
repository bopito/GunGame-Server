package server.game.base.projectile;

import server.game.base.projectile.Projectile;

/**
 * Handles projectile actions, such as movement.
 */
public class ProjectileHandler {
    public void updateProjectilePosition(Projectile projectile) {
        double radians = Math.toRadians(projectile.getAngle());
        projectile.setX(projectile.getX() + projectile.getSpeed() * Math.cos(radians));
        projectile.setZ(projectile.getZ() + projectile.getSpeed() * Math.sin(radians));
    }
}
