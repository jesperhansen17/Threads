package ass3;

import java.util.ArrayList;

/**
 * Created by JesperHansen on 2015-11-26.
 */
public class Truck extends Thread {
    private Storage storage;
    private GUISemaphore guiSemaphore;
    private ArrayList<FoodItem> truckStorage;
    private int TRUCK_STORAGE = 10;

    public Truck(Storage storage, GUISemaphore guiSemaphore) {
        this.truckStorage = new ArrayList<FoodItem>();
        this.storage = storage;
        this.guiSemaphore = guiSemaphore;
    }

    // Trucks retreives 10 items from storage
    public void run() {
        while (true) {
            try {
                guiSemaphore.updateTruckStatus("Truck loading cargo");

                // Do this if the truck cargo is not full
                while (truckStorage.size() != TRUCK_STORAGE) {
                    // Wait here if the storage is empty
                    while (storage.getTruckStatus() == 0) {
                        guiSemaphore.updateDeliver("Storage empty");
                    }

                    int items = 0;
                    guiSemaphore.updateDeliver("Loading");
                    FoodItem foodItem = storage.FetchStorage();     // Retreive FoodItem from storage
                    guiSemaphore.updateCargoInfo(foodItem.getWeight(), foodItem.getVolume(), ++items, foodItem.getName());  // Update GUI
                    truckStorage.add(foodItem); // Add FoodItem to trucks cargo
                    Thread.sleep(500);
                }

                // Truck leaves storage, reset cargos own storage
                guiSemaphore.resetCargoInfo(0, 0, 0, "");
                guiSemaphore.updateTruckStatus("Cargo is full(items)");
                guiSemaphore.updateDeliver("Delivering food");
                truckStorage.clear();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
