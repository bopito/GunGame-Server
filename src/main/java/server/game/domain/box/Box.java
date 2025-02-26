package server.game.domain.box;

import lombok.Getter;
import lombok.Setter;
import server.game.base.entity.Entity;
import server.game.domain.weapon.Weapon;

/**
 * Represents a destructible box that drops a weapon when destroyed.
 */
@Getter
@Setter
public class Box extends Entity {
    private int hp;
    private boolean isDestroyed;
    private Weapon droppedWeapon;

    /**
     * Initializes a box at a specific location with a given HP and a weapon.
     */
    public Box(int hp, Weapon weapon, double x, double z) {
        this.hp = hp;
        this.isDestroyed = false;
        this.droppedWeapon = weapon;

        this.x = x;
        this.y = 0;
        this.z = z;
    }

    /**
     * Applies damage to the box.
     */
    public void takeDamage(int damage) {
        if (isDestroyed) return;

        this.hp -= damage;
        System.out.println("[Box] Took " + damage + " damage! Remaining HP: " + this.hp);

        if (this.hp <= 0) {
            destroy();
        }
    }

    /**
     * Simulates destroying the box, making the weapon available for pickup.
     */
    public void destroy() {
        this.isDestroyed = true;
        System.out.println("[Box] Destroyed at (" + x + ", " + z + ")! Dropped weapon: " + droppedWeapon.getName());
    }

    @Override
    public void update() {

    }
}
