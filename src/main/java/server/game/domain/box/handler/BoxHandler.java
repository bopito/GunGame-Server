package server.game.domain.box.handler;

import server.game.domain.box.Box;
import server.game.domain.box.BoxManager;
import server.game.domain.player.Player;

/**
 * Handles player interactions with boxes (attacking and dropping weapons).
 */
public class BoxHandler {
    private final BoxManager boxManager;

    public BoxHandler(BoxManager boxManager) {
        this.boxManager = boxManager;
    }

    /**
     * Handles damage dealt to a box by a player.
     */
    public void attackBox(Player player, Box box, int damage) {
        if (!box.isDestroyed()) {
            box.takeDamage(damage);
            System.out.println("[BoxHandler] " + player.getId() + " attacked a box! HP remaining: " + box.getHp());

            if (box.isDestroyed()) {
                System.out.println("[BoxHandler] Box destroyed at (" + box.getX() + ", " + box.getZ() + ")");
                boxManager.destroyBox(box);
            }
        } else {
            System.out.println("[BoxHandler] Box is already destroyed.");
        }
    }
}
