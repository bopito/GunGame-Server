package server.game.domain.player.manager;

import server.game.domain.player.Player;
import server.game.domain.weapon.Weapon;
import server.game.domain.weapon.handler.WeaponHandler;
import server.game.domain.weapon.WeaponManager;

import java.util.Optional;

/**
 * Manages player's weapon handling.
 */
public class PlayerWeaponManager {
    private final Player player;
    private final WeaponManager weaponManager;
    private final WeaponHandler weaponHandler;

    public PlayerWeaponManager(Player player) {
        this.player = player;
        this.weaponManager = new WeaponManager();
        this.weaponHandler = new WeaponHandler(weaponManager);
    }

    /**
     * Equips a weapon to the player.
     */
    public void equipWeapon(Weapon weapon) {
        dropCurrentWeapon(); // ✅ 기존 무기 버리기
        player.setCurrentWeapon(weapon);
        System.out.println("[PlayerWeaponManager] " + player.getId() + " equipped " + weapon.getName());
    }

    /**
     * Shoots the player's current weapon.
     */
    public void shootBullet() {
        weaponHandler.shootWeapon(player);
    }

    /**
     * Allows the player to pick up a weapon from the ground.
     */
    public void pickUpWeapon(Weapon weapon) {
        if (!weaponManager.getDroppedWeapons().contains(weapon)) {
            System.out.println("[PlayerWeaponManager] The weapon is not available on the ground.");
            return;
        }

        equipWeapon(weapon);
        weaponManager.removeDroppedWeapon(weapon); //
        System.out.println("[PlayerWeaponManager] " + player.getId() + " picked up " + weapon.getName());
    }

    /**
     * Drops the player's current weapon on the ground.
     */
    private void dropCurrentWeapon() {
        Optional.ofNullable(player.getCurrentWeapon()).ifPresent(oldWeapon -> {
            weaponManager.addDroppedWeapon(oldWeapon);
            System.out.println("[PlayerWeaponManager] " + player.getId() + " dropped previous weapon: " + oldWeapon.getName());
        });
    }
}
