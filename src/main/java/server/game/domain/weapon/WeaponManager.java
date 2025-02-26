package server.game.domain.weapon;

import java.util.*;
import org.springframework.stereotype.Component;

/**
 * Manages weapons for the game.
 */
@Component
public class WeaponManager {
    private final Map<String, Weapon> weapons = new HashMap<>();
    private final List<Weapon> droppedWeapons = new ArrayList<>();

    // Default weapon
    private final Weapon defaultWeapon;

    /**
     * Initializes default weapons.
     */
    public WeaponManager() {
        defaultWeapon = new Weapon("Pistol", 10, 1000, 100, 2.0, 0.5, 60);
        addWeapon(defaultWeapon);

        addWeapon(new Weapon("Rifle", 6, 1200, 150, 2.5, 0.4, 120));
        addWeapon(new Weapon("Sniper", 25, 2000, 300, 3.5, 1.5, 25));
        addWeapon(new Weapon("Rocket Launcher", 50, 800, 200, 4.0, 2.5, 10));
        addWeapon(new Weapon("Knife", 80, 0, 30, 0, 0.3, 0)); // Melee weapon
    }

    public void addWeapon(Weapon weapon) {
        weapons.put(weapon.getId(), weapon);
    }

    public Weapon getWeapon(String id) {
        return weapons.get(id);
    }

    public Weapon getDefaultWeapon() {
        return defaultWeapon;
    }

    public void removeWeapon(String id) {
        weapons.remove(id);
    }

    public void addDroppedWeapon(Weapon weapon) {
        droppedWeapons.add(weapon);
        System.out.println("[WeaponManager] A weapon has been dropped: " + weapon.getName());
    }

    public void removeDroppedWeapon(Weapon weapon) {
        droppedWeapons.remove(weapon);
        System.out.println("[WeaponManager] A dropped weapon has been picked up: " + weapon.getName());
    }

    public List<Weapon> getDroppedWeapons() {
        return new ArrayList<>(droppedWeapons);
    }
}
