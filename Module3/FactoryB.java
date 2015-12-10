package ass3;

import java.util.Random;
import java.util.concurrent.Semaphore;

// Factory B that produces FoodItems
public class FactoryB extends Thread {
    private Storage storage;
    private GUISemaphore guiSemaphore;
    private Controller controller;
    private boolean RUNNING;

    public FactoryB(Storage storage, GUISemaphore guiSemaphore, Controller controller) {
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
            guiSemaphore.toggleFactoryB(false);     // Disable GUI button
            try {
                // Checks if the storage is full
                while (storage.getFactoryStatus() == 0) {
                    guiSemaphore.updateFactoryBStatus("Storage full, wait");
                }
                guiSemaphore.updateFactoryBStatus("Producing");

                // Retreive a FoodItem and push it to the storage
                storage.PushStorage(controller.getFoodItem(rand.nextInt(20)));
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Only happens when the factory is stopped
        guiSemaphore.toggleFactoryB(true);
        guiSemaphore.updateFactoryBStatus("Factory B stopped");
    }
}
