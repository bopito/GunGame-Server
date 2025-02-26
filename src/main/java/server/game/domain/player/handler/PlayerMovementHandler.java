package server.game.domain.player.handler;/*
 * created by seokhyun on 2025-02-26.
 */

import server.game.domain.player.Player;

/**
 * Handles player movement based on key inputs.
 */
public class PlayerMovementHandler {
    private final Player player;

    public PlayerMovementHandler(Player player) {
        this.player = player;
    }

    public void updateMovement() {
        if (player.getKeys().getOrDefault("w", false)) player.setZ(player.getZ() - player.getSpeed());
        if (player.getKeys().getOrDefault("s", false)) player.setZ(player.getZ() + player.getSpeed());
        if (player.getKeys().getOrDefault("a", false)) player.setX(player.getX() - player.getSpeed());
        if (player.getKeys().getOrDefault("d", false)) player.setX(player.getX() + player.getSpeed());
    }
}
