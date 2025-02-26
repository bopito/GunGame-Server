package server.game.base.projectile;

import server.game.base.projectile.Projectile;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all projectiles in the game.
 */
public class ProjectileManager {
    private final Map<String, Projectile> projectiles = new HashMap<>();

    public void addProjectile(Projectile projectile) {
        projectiles.put(projectile.getId(), projectile);
    }

    public Projectile getProjectile(String id) {
        return projectiles.get(id);
    }

    public void removeProjectile(String id) {
        projectiles.remove(id);
    }

    public void updateProjectiles() {
        for (Projectile projectile : projectiles.values()) {
            projectile.update();
        }
    }
}
