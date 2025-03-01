package server.game.domain.box;

import server.game.domain.weapon.Weapon;
import server.game.domain.weapon.WeaponManager;
import server.game.core.GameEngine;

import java.util.*;

/**
 * Manages box generation, destruction, and loot drop.
 */
public class BoxManager {
    private static List<WeaponDropChance> WEAPON_DROP_TABLE;
    private final List<Box> activeBoxes = new ArrayList<>();
    private final WeaponManager weaponManager;
    private final GameEngine gameEngine;
    private static final Random random = new Random();

    // set map size
    private static final int MAP_SIZE_X = 50;
    private static final int MAP_SIZE_Z = 50;

    // weapon percentage
    private void initializeWeaponDropTable() {
        WEAPON_DROP_TABLE = List.of(
                new WeaponDropChance(weaponManager.getWeaponByName("Pistol"), 40),
                new WeaponDropChance(weaponManager.getWeaponByName("Rifle"), 30),
                new WeaponDropChance(weaponManager.getWeaponByName("Sniper"), 15),
                new WeaponDropChance(weaponManager.getWeaponByName("Rocket Launcher"), 10),
                new WeaponDropChance(weaponManager.getWeaponByName("Knife"), 5)
        );
    }

    public BoxManager(WeaponManager weaponManager, GameEngine gameEngine) {
        this.weaponManager = weaponManager;
        this.gameEngine = gameEngine;
        initializeWeaponDropTable(); // ✅ Initialize the weapon drop table
    }

    /**
     * Spawns a new box at a random position with random HP and a random weapon.
     */
    public Box spawnBox() {
        Weapon randomWeapon = getRandomWeaponByChance();
//        int randomHp = 50 + random.nextInt(50); // ✅ Box health between 50-100
        int randomHp = 100; // ✅ Box health between 50-100

        // create box to random location
        double randomX = random.nextDouble() * MAP_SIZE_X;
        double randomZ = random.nextDouble() * MAP_SIZE_Z;

        Box newBox = new Box(randomHp, randomWeapon, randomX, randomZ);
        activeBoxes.add(newBox);

        gameEngine.onBoxSpawned(newBox);

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
                return drop.getWeapon();
            }
        }

        // basic weapon (prevent null)
        System.err.println("[BoxManager] Warning: No weapon selected, assigning default weapon.");
        return weaponManager.getDefaultWeapon();
    }


    /**
     * Helper class to store weapon drop chance.
     */
    public class WeaponDropChance {
        private final Weapon weapon;
        private final int chance;

        public WeaponDropChance(Weapon weapon, int chance) {
            this.weapon = weapon;
            this.chance = chance;
        }

        public Weapon getWeapon() {
            return weapon;
        }

        public int getChance() {
            return chance;
        }
    }
}
