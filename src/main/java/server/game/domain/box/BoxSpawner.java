package server.game.domain.box;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles automatic spawning of boxes at random intervals (6-8 seconds).
 */
public class BoxSpawner {
    private final BoxManager boxManager;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final Random random = new Random();

    public BoxSpawner(BoxManager boxManager) {
        this.boxManager = boxManager;
    }

    /**
     * Starts automatic box spawning every 6 to 8 seconds.
     */
    public void startSpawning() {
        scheduler.scheduleAtFixedRate(() -> {
            if (boxManager.getActiveBoxCount() < 5) { // ✅ Check if there is room for new boxes
                boxManager.spawnBox();
            } else {
                System.out.println("[BoxSpawner] Maximum number of boxes reached. No new box will be spawned.");
            }

            int nextDelay = 30 + random.nextInt(10); // Random spawn time between 30-40 seconds
            System.out.println("[BoxSpawner] Next box will spawn in " + nextDelay + " seconds.");
        }, 0, 30 + random.nextInt(10), TimeUnit.SECONDS);
    }
}
