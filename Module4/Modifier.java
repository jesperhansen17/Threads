package ass4;

/**
 * Class that extends Thread and modifies String data to the BoundedBuffer
 */
public class Modifier extends Thread {
    private BoundedBuffer buffer;
    private int count;

    /**
     * Constructor that initialize the buffer instance and the number of String instance
     * @param buffer            BoundedBuffer that monitors the Threads
     * @param nbrOfStrings      Number of Strings in the List
     */
    public Modifier(BoundedBuffer buffer, int nbrOfStrings) {
        this.buffer = buffer;
        this.count = nbrOfStrings;

    }

    /**
     * Run method that loops through the list and modifies the buffer in each iteration
     */
    public void run() {
        for (int i = 0; i < count; i++)
        try {
            // Tries to modify the buffer
            buffer.modify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
