package server.game.domain.weapon;

import java.util.*;

/**
 * Manages weapons for the game.
 */
public class WeaponManager {
    private final Map<String, Weapon> weapons = new HashMap<>();
    private final List<Weapon> droppedWeapons = new ArrayList<>(); // ✅ 맵에 떨어진 무기 목록

    /**
     * Initializes default weapons based on provided table.
     */
    public WeaponManager() {
        addWeapon(new Weapon("Bucket (Pistol)", 10, 1000, 100, 2.0, 0.5, 60));
        addWeapon(new Weapon("Basket (Rifle)", 6, 1200, 150, 2.5, 0.4, 120));
        addWeapon(new Weapon("Backpack (Sniper)", 25, 2000, 300, 3.5, 1.5, 25));
        addWeapon(new Weapon("Luggage (Rocket)", 50, 800, 200, 4.0, 2.5, 10));
        addWeapon(new Weapon("Toy Hammer (Knife)", 80, 0, 30, 0, 0.3, 0)); // Knife has no ammo
    }

    public void addWeapon(Weapon weapon) {
        weapons.put(weapon.getId(), weapon);
    }

    public Weapon getWeapon(String id) {
        return weapons.get(id);
    }

    public void removeWeapon(String id) {
        weapons.remove(id);
    }

    /**
     * Adds a dropped weapon to the map.
     */
    public void addDroppedWeapon(Weapon weapon) {
        droppedWeapons.add(weapon);
        System.out.println("[WeaponManager] A weapon has been dropped: " + weapon.getName());
    }

    /**
     * Removes a weapon from the dropped weapons list (e.g., when picked up).
     */
    public void removeDroppedWeapon(Weapon weapon) {
        droppedWeapons.remove(weapon);
        System.out.println("[WeaponManager] A dropped weapon has been picked up: " + weapon.getName());
    }

    /**
     * Returns a list of all dropped weapons on the map.
     */
    public List<Weapon> getDroppedWeapons() {
        return new ArrayList<>(droppedWeapons);
    }
}
