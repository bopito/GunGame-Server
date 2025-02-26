package server.game.domain.player.manager;/*
 * created by seokhyun on 2025-02-26.
 */

import server.game.domain.player.Player;

/**
 * Manages the player's health, handling damage and healing.
 */
public class PlayerHealthManager {
    private final Player player;
    private final int maxHealth = 100;

    public PlayerHealthManager(Player player) {
        this.player = player;
    }

    public void takeDamage(int damage) {
        player.setHealth(Math.max(0, player.getHealth() - damage));
        if (player.getHealth() == 0) {
            System.out.println("[Player] " + player.getId() + " has been eliminated!");
        }
    }

    public void heal(int amount) {
        int newHealth = Math.min(player.getHealth() + amount, maxHealth);
        player.setHealth(newHealth);
        System.out.println("[Player] Healed: " + amount + " HP. Current HP: " + player.getHealth());
    }
}
