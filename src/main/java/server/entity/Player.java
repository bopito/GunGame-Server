package server.entity;

import java.util.Iterator;
import java.util.Map;

public class Player extends Entity {
    
    private Map<String, Boolean> keys;
    private int team;
    private int score;
    private int health;
    private int speed;

    public Player(int team) {
        super();
        this.keys = Map.of("w", false, "a", false, "s", false, "d", false);
        this.team = team;
        this.score = 0;
        this.health = 100;
        this.speed = 1;
        
    }

    public int getTeam() { return team; }
    public Map<String, Boolean> getKeys() { return keys; }
    public void setKeys(Map<String, Boolean> keys) { this.keys = keys; }

    @Override
    public void update() {
        if (keys.getOrDefault("w", false)) z -= speed;
        if (keys.getOrDefault("s", false)) z += speed;
        if (keys.getOrDefault("a", false)) x -= speed;
        if (keys.getOrDefault("d", false)) x += speed;
    }

    public void handleCollision(Entity otherEntity) {

    }

}
