package server.game.domain.box;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles automatic spawning of boxes at random intervals (6-8 seconds).
 */
@Component
public class BoxSpawner {
    private final BoxManager boxManager;
    private ScheduledExecutorService scheduler;
    private static final Random random = new Random();
    private boolean isRunning = false; // ✅ Track if the spawner is running
    private BoxSpawnListener boxSpawnListener; //add listener

    @Autowired
    public BoxSpawner(BoxManager boxManager) {
        this.boxManager = boxManager;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void setBoxSpawnListener(BoxSpawnListener listener) {
        this.boxSpawnListener = listener;
    }

    /**
     * Starts automatic box spawning every 6 to 8 seconds.
     */
    public synchronized void startSpawning() {
        if (isRunning) return ; // block double execute
        isRunning = true;

        if (scheduler.isShutdown() || scheduler.isTerminated()) {
            scheduler = Executors.newSingleThreadScheduledExecutor(); // create new scheduler
        }

        scheduler.scheduleAtFixedRate(() -> {
            if (boxManager.getActiveBoxCount() < 5) { // Check if there is room for new boxes
                Box newBox = boxManager.spawnBox();
                if (boxSpawnListener != null && newBox != null) {
                    boxSpawnListener.onBoxSpawned(newBox); // call listener
                }
            } else {
                System.out.println("[BoxSpawner] Maximum number of boxes reached. No new box will be spawned.");
            }

            int nextDelay = 30 + random.nextInt(10); // Random spawn time between 30-40 seconds
            System.out.println("[BoxSpawner] Next box will spawn in " + nextDelay + " seconds.");
        }, 0, 30 + random.nextInt(10), TimeUnit.SECONDS);
    }

    /**
     * Stops box spawning when no players are online.
     */
    public synchronized void stopSpawning() {
        if (!isRunning) return; // ✅ Prevent duplicate stop
        isRunning = false;

        scheduler.shutdownNow(); // ✅ Stop scheduled tasks
        System.out.println("[BoxSpawner] Box spawning stopped.");
    }

    public interface BoxSpawnListener {
        void onBoxSpawned(Box box);
    }

}
