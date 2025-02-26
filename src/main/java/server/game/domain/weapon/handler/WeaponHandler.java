package server.game.domain.weapon.handler;

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
    public void shootWeapon(Player player) {
        Weapon weapon = player.getCurrentWeapon();
        if (weapon == null) {
            System.out.println("[Weapon] No weapon equipped!");
            return;
        }

        if (weapon.canShoot()) {
            weapon.setCurrentAmmo(weapon.getCurrentAmmo() - 1);
            System.out.println("[Weapon] " + player.getId() + " fired a bullet with " + weapon.getName());
        } else {
            System.out.println("[Weapon] Out of ammo! Reload required.");
        }
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
        weaponManager.removeDroppedWeapon(weapon); // ✅ 맵에서 무기 제거
        System.out.println("[WeaponHandler] " + player.getId() + " picked up " + weapon.getName());

        return droppedWeapon;
    }
}
