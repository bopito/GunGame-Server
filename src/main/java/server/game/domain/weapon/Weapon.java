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
    private final int maxAmmo; // Magazine capacity
    private int currentAmmo; // Bullets in the current magazine
    private int reserveAmmo; // Total available bullets in reserve

    /**
     * Initializes a new weapon.
     */
    public Weapon(String name, int damage, double bulletSpeed, int range, double reloadTime, double rateOfFire, int maxAmmo, int reserveAmmo) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.range = range;
        this.reloadTime = reloadTime;
        this.rateOfFire = rateOfFire;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        this.reserveAmmo = reserveAmmo;
    }

    /**
     * Check if the weapon can fire.
     */
    public boolean canShoot() {
        return currentAmmo > 0;
    }

    /**
     * Reload the weapon.
     */
    public void reload() {
        if (reserveAmmo <= 0) {
            System.out.println("[Weapon] " + name + " cannot reload - no reserve ammo left!");
            return;
        }

        int neededAmmo = maxAmmo - currentAmmo; // Bullets needed to fill the magazine

        if (reserveAmmo >= neededAmmo) {
            currentAmmo += neededAmmo;
            reserveAmmo -= neededAmmo;
        } else {
            currentAmmo += reserveAmmo;
            reserveAmmo = 0;
        }

        System.out.println("[Weapon] " + name + " reloaded: " + currentAmmo + "/" + reserveAmmo);
    }
}
