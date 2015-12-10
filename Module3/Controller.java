package ass3;

import javax.swing.*;


// Starts and controll the application
public class Controller {
    private GUISemaphore guiSemaphore;
    private Storage storage;
    FactoryA factoryA;
    FactoryB factoryB;
    private FoodItem foodBuffer[];

    // Init all the instance variable and start the GUI
    public Controller() {
        initFood();
        this.guiSemaphore = new GUISemaphore(this);
        this.storage = new Storage(guiSemaphore);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                guiSemaphore.Start();
            }
        });
    }

    // Method starts factory A
    public void startFactoryA() {
        factoryA = new FactoryA(storage, guiSemaphore, this);
        factoryA.startStop(true);
        factoryA.start();
    }

    // Method stops factory A
    public void stopFactoryA() {
        factoryA.startStop(false);
    }

    // Method starts factory B
    public void startFactoryB() {
        factoryB = new FactoryB(storage, guiSemaphore, this);
        factoryB.startStop(true);
        factoryB.start();
    }

    // Method stops factory B
    public void stopFactoryB() {
        factoryB.startStop(false);
    }

    // Method starts truck delivery
    public void startTruck() {
        Truck truck = new Truck(storage, guiSemaphore);
        truck.start();
    }

    // Init a list of food items that the factory can produce
    private void initFood() {
        foodBuffer = new FoodItem[20];
        foodBuffer[0] = new FoodItem("Banana", 10.2, 15);
        foodBuffer[1] = new FoodItem("Orange", 5.2, 6);
        foodBuffer[2] = new FoodItem("Raisins", 3.1, 2);
        foodBuffer[3] = new FoodItem("Milk", 10.2, 19);
        foodBuffer[4] = new FoodItem("Butter", 1, 2);
        foodBuffer[5] = new FoodItem("Bread", 5.2, 5);
        foodBuffer[6] = new FoodItem("Cream", 8, 7);
        foodBuffer[7] = new FoodItem("Soda", 10, 1);
        foodBuffer[8] = new FoodItem("Candles", 11.2, 2);
        foodBuffer[9] = new FoodItem("Candy", 19, 4);
        foodBuffer[10] = new FoodItem("Beer", 11.1, 45);
        foodBuffer[11] = new FoodItem("Apple", 4.5, 28);
        foodBuffer[12] = new FoodItem("Pear", 6.7, 1);
        foodBuffer[13] = new FoodItem("Donuts", 7.8, 18);
        foodBuffer[14] = new FoodItem("Jam", 9.1, 55);
        foodBuffer[15] = new FoodItem("Mayo", 4.2, 2);
        foodBuffer[16] = new FoodItem("Egg", 5.2, 18);
        foodBuffer[17] = new FoodItem("Salt", 0.65, 49);
        foodBuffer[18] = new FoodItem("Almonds", 2.3, 2);
        foodBuffer[19] = new FoodItem("Beef", 6.4, 1);
    }

    // Factory can retreive a food item from the list here
    public FoodItem getFoodItem(int i) {
        return foodBuffer[i];
    }
}
