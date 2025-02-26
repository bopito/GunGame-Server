package server.game.domain.weapon;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

/**
 * Represents a weapon in the game.
 */
@Getter
@Setter
public class Weapon {
    private final String id;
    private final String name;
    private final double bulletSpeed;
    private final int damage;
    private final double reloadTime;
    private final double rateOfFire;
    private final int range;
    private final int maxAmmo;
    private int currentAmmo;

    /**
     * Initializes a new weapon.
     */
    public Weapon(String name, int damage, double bulletSpeed, int range, double reloadTime, double rateOfFire, int maxAmmo) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.range = range;
        this.reloadTime = reloadTime;
        this.rateOfFire = rateOfFire;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
    }

    /**
     * Checks if the weapon can shoot.
     */
    public boolean canShoot() {
        return currentAmmo > 0;
    }

    /**
     * Reloads the weapon.
     */
    public void reload() {
        this.currentAmmo = maxAmmo;
        System.out.println("[Weapon] " + name + " reloaded!");
    }
}
