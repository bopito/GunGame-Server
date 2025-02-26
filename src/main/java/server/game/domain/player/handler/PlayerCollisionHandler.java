package server.game.domain.player.handler;/*
 * created by seokhyun on 2025-02-26.
 */


import server.game.base.entity.Entity;
import server.game.domain.player.Player;

/**
 * Handles player collisions with other entities.
 */
public class PlayerCollisionHandler {
    private final Player player;

    public PlayerCollisionHandler(Player player) {
        this.player = player;
    }

    public void handleCollision(Entity otherEntity) {
        System.out.println("[Player] Collision detected with entity: " + otherEntity.getId());
    }
}
