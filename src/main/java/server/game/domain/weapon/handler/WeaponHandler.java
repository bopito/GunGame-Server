package server.game.domain.weapon.handler;

import server.game.domain.bullet.Bullet;
import server.game.domain.weapon.Weapon;
import server.game.domain.weapon.WeaponManager;
import server.game.domain.player.Player;

import java.util.Optional;

/**
 * Handles actions related to weapons.
 */
public class WeaponHandler {
    private final WeaponManager weaponManager;

    public WeaponHandler(WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    /**
     * Fires a bullet from the player's current weapon.
     */
    public Bullet shootWeapon(Player player) {
        Weapon weapon = player.getCurrentWeapon();
        if (weapon == null) {
            System.out.println("[Weapon] No weapon equipped!");
            return null;
        }

        if (!weapon.canShoot()) {
            System.out.println("[Weapon] Out of ammo! Reload required.");
            return null;
        }

        weapon.setCurrentAmmo(weapon.getCurrentAmmo() - 1);
        System.out.println("[Weapon] " + player.getId() + " fired a bullet with " + weapon.getName());

        // Retrieve player position and direction
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        double angle = player.getAngle();

        // Create and return bullet
        return new Bullet(x, y, z, angle, weapon.getBulletSpeed(), weapon.getDamage(), player.getId(), weapon.getRange());
    }

    public void reloadWeapon(Player player){
        Weapon weapon = player.getCurrentWeapon();
        if (weapon == null) {
            System.out.println("[Weapon] No weapon equipped!");
            return;
        }
        weapon.reload();
    }

    /**
     * Picks up a dropped weapon from the map.
     */
    public Optional<Weapon> pickUpDroppedWeapon(Player player, Weapon weapon) {
        if (!weaponManager.getDroppedWeapons().contains(weapon)) {
            System.out.println("[WeaponHandler] The weapon is not on the ground.");
            return Optional.empty();
        }

        Optional<Weapon> droppedWeapon = Optional.ofNullable(player.getCurrentWeapon());
        player.setCurrentWeapon(weapon);
        weaponManager.removeDroppedWeapon(weapon); // Remove weapon from the map
        System.out.println("[WeaponHandler] " + player.getId() + " picked up " + weapon.getName());

        return droppedWeapon;
    }
}
