package server.entity;

import java.util.UUID;

public class Projectile extends Entity {
    private final String shooterId;
    private String type;
    private int speed;
    private int damage;

    public Projectile(String shooterId, int x, int y, double angle, String type, int speed, int damage) {
        this.id = UUID.randomUUID().toString();
        this.shooterId = shooterId;
        this.type = type;
        this.speed = speed;
        this.damage = damage;
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getShooterId() { return shooterId; }
    public String getType() { return type; }
    public int getSpeed() { return speed; }
    public int getDamage() { return damage; }

    public void update() {
        x += speed * Math.cos(Math.toRadians(angle));
        z += speed * Math.sin(Math.toRadians(angle));
    }
}
