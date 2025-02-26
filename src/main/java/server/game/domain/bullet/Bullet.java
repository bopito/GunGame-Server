package server.game.domain.bullet;

import server.game.base.projectile.Projectile;

public class Bullet extends Projectile {
    public Bullet(double x, double y, double z, double angle, double speed, int damage, String shooterId) {
        super(x, y, z, angle, speed, damage, shooterId);
    }
}
