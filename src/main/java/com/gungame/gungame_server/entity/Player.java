package com.gungame.gungame_server.entity;

import java.util.Map;
import java.util.UUID;

public class Player {
    private String id; // UUID-based Player ID
    private int x, y;
    private double angle;
    private int team;
    private Map<String, Boolean> keys;

    public Player(int team) {
        this.id = UUID.randomUUID().toString();
        this.team = team;
        this.x = 0;
        this.y = 0;
        this.angle = 0;
        this.keys = Map.of("w", false, "a", false, "s", false, "d", false);
    }

    // Getters and Setters
    public String getId() { return id; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public double getAngle() { return angle; }
    public void setAngle(double angle) { this.angle = angle; }
    public int getTeam() { return team; }
    public Map<String, Boolean> getKeys() { return keys; }
    public void setKeys(Map<String, Boolean> keys) { this.keys = keys; }
}
