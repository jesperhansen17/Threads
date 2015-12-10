package ass3;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

// Class handles the Storage
public class Storage {
    private LinkedList<FoodItem> queue;
    private GUISemaphore guiSemaphore;
    private Semaphore mutex;
    private Semaphore rdSem;
    private Semaphore wrSem;

    public Storage(GUISemaphore guiSemaphore) {
        this.guiSemaphore = guiSemaphore;
        queue = new LinkedList<FoodItem>();
        mutex = new Semaphore(1);
        wrSem = new Semaphore(100); // Empty spaces
        rdSem = new Semaphore(0);   // Occupied spaces

    }

    // Returns the number of available spaces in the storage
    public int getFactoryStatus() {
        return wrSem.availablePermits();
    }

    // Returns the number of occupied spaces
    public int getTruckStatus() {
        return rdSem.availablePermits();
    }

    // Adds the FoodItem to the storage
    public void PushStorage(FoodItem foodItem) {
        try {
            wrSem.acquire(); // Decrement available spaces
            mutex.acquire(); // Locks the method so that only one factory can enter

            queue.addFirst(foodItem);
            guiSemaphore.updateBufferStatus(queue.size());  // Update progressbar
            System.out.println("Added: " + foodItem.getName());

            mutex.release();    // Release the method so that the other factory can use it
            rdSem.release();    // Increment number of occupied spaces
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Retreives FoodItem from storage
    public FoodItem FetchStorage() {
        try {
            rdSem.acquire(); // Decrement number of occupied spaces

            guiSemaphore.updateBufferStatus(queue.size());  // Update progressbar
            FoodItem foodItem = queue.removeLast();         // Remove the last FoodItem in storage
            System.out.println("Fetch Item");

            wrSem.release();    // Increment number of available spaces
            return foodItem;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
