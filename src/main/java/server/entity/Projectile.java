package server.entity;

import java.util.UUID;

public class Projectile {
    private final String id;
    private final String shooterId;
    private int x, y;
    private double angle;
    private String type;  // e.g., "bullet", "rocket"
    private int speed;
    private int damage;

    public Projectile(String shooterId, int x, int y, double angle, String type, int speed, int damage) {
        this.id = UUID.randomUUID().toString();
        this.shooterId = shooterId;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.type = type;
        this.speed = speed;
        this.damage = damage;
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getShooterId() { return shooterId; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public double getAngle() { return angle; }
    public void setAngle(double angle) { this.angle = angle; }
    public String getType() { return type; }
    public int getSpeed() { return speed; }
    public int getDamage() { return damage; }

    public void update() {
        x += speed * Math.cos(Math.toRadians(angle));
        y += speed * Math.sin(Math.toRadians(angle));
    }
}
