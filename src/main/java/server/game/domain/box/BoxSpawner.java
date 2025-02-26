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
            boxManager.spawnBox();
            int nextDelay = 6 + random.nextInt(3); // 6~8초 랜덤
            System.out.println("[BoxSpawner] Next box will spawn in " + nextDelay + " seconds.");
        }, 0, 6 + random.nextInt(3), TimeUnit.SECONDS);
    }
}
