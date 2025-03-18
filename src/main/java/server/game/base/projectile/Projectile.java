package server.game.base.projectile;

import lombok.Getter;
import server.game.base.entity.Entity;

/**
 * Represents a general projectile in the game (e.g., bullets, energy shots, missiles).
 */
@Getter
public abstract class Projectile extends Entity {
    protected double speed;
    protected int damage;
    protected String shooterId;
    protected double range; // Maximum range the bullet can travel
    protected boolean active;

    /**
     * Initializes a new projectile.
     */
    public Projectile(double x, double y, double z, double angle, double speed, int damage, String shooterId, double range) {
        super(); // Call Entity constructor
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
        this.speed = speed;
        this.damage = damage;
        this.shooterId = shooterId;
        this.range = range;
    }

    @Override
    public void update() {
        double radians = Math.toRadians(angle);
        this.x += speed * Math.cos(radians);
        this.z += speed * Math.sin(radians);
    }

    public void setActive(boolean active){
        this.active = active;
    }
}
