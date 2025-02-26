package server.game.domain.box;

import server.game.domain.weapon.Weapon;
import server.game.domain.weapon.WeaponManager;

import java.util.*;

/**
 * Manages box generation, destruction, and loot drop.
 */
public class BoxManager {
    private final List<Box> activeBoxes = new ArrayList<>();
    private final WeaponManager weaponManager;
    private static final Random random = new Random();

    // set map size
    private static final int MAP_SIZE_X = 100;
    private static final int MAP_SIZE_Z = 100;

    // weapon percentage
    private static final List<WeaponDropChance> WEAPON_DROP_TABLE = List.of(
            new WeaponDropChance("Bucket (Pistol)", 40),
            new WeaponDropChance("Basket (Rifle)", 30),
            new WeaponDropChance("Backpack (Sniper)", 15),
            new WeaponDropChance("Luggage (Rocket)", 10),
            new WeaponDropChance("Toy Hammer (Knife)", 5)
    );

    public BoxManager(WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    /**
     * Spawns a new box at a random position with random HP and a random weapon.
     */
    public Box spawnBox() {
        Weapon randomWeapon = getRandomWeaponByChance();
        int randomHp = 50 + random.nextInt(50); // ✅ Box health between 50-100

        // create box to random location
        double randomX = random.nextDouble() * MAP_SIZE_X;
        double randomZ = random.nextDouble() * MAP_SIZE_Z;

        Box newBox = new Box(randomHp, randomWeapon, randomX, randomZ);
        activeBoxes.add(newBox);
        System.out.println("[BoxManager] Spawned new box at (" + randomX + ", " + randomZ + ") with HP: " + randomHp + " containing: " + randomWeapon.getName());
        return newBox;
    }

    /**
     * Destroys a box and removes it from active boxes, dropping the weapon onto the map.
     */
    public void destroyBox(Box box) {
        if (!box.isDestroyed()) {
            box.destroy();
            activeBoxes.remove(box);

            // create weapon of random rate
            Weapon droppedWeapon = getRandomWeaponByChance();
            weaponManager.addDroppedWeapon(droppedWeapon);
            System.out.println("[BoxManager] Dropped weapon: " + droppedWeapon.getName() + " at (" + box.getX() + ", " + box.getZ() + ")");
        }
    }

    /**
     * Selects a weapon based on drop chance.
     */
    private Weapon getRandomWeaponByChance() {
        int roll = random.nextInt(100);
        int cumulativeChance = 0;

        for (WeaponDropChance drop : WEAPON_DROP_TABLE) {
            cumulativeChance += drop.getChance();
            if (roll < cumulativeChance) {
                return weaponManager.getWeapon(drop.getWeaponName());
            }
        }
        return weaponManager.getWeapon("Bucket (Pistol)"); // 기본값
    }

    /**
     * Helper class to store weapon drop chance.
     */
    private static class WeaponDropChance {
        private final String weaponName;
        private final int chance;

        public WeaponDropChance(String weaponName, int chance) {
            this.weaponName = weaponName;
            this.chance = chance;
        }

        public String getWeaponName() {
            return weaponName;
        }

        public int getChance() {
            return chance;
        }
    }
}
