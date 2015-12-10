package ass3;

import java.util.Random;
import java.util.concurrent.Semaphore;

// FactoryA creates FoodItem
public class FactoryA extends Thread {
    private Storage storage;
    private GUISemaphore guiSemaphore;
    private Controller controller;
    private boolean RUNNING;

    public FactoryA(Storage storage, GUISemaphore guiSemaphore, Controller controller) {
        this.storage = storage;
        this.guiSemaphore = guiSemaphore;
        this.controller = controller;
    }

    public void startStop(boolean startStop) {
        this.RUNNING = startStop;
    }

    @Override
    public void run() {
        Random rand = new Random();
        // Runs as long boolean RUNNING is true
        while (RUNNING) {
            try {
                guiSemaphore.toggleFactoryA(false);     // Disable GUI button

                // Checks if the storage is full
                while (storage.getFactoryStatus() == 0) {
                    guiSemaphore.updateFactoryAStatus("Storage full, wait");
                }
                guiSemaphore.updateFactoryAStatus("Producing");

                // Retreive a FoodItem and push it to the storage
                storage.PushStorage(controller.getFoodItem(rand.nextInt(20)));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Only happens when the factory is stopped
        guiSemaphore.toggleFactoryA(true);
        guiSemaphore.updateFactoryAStatus("Factory A stopped");
    }
}
