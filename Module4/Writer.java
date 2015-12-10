package ass4;

import java.util.List;

/**
 * Class that extends Thread and writes String data to the BoundedBuffer
 */
public class Writer extends Thread {
    private BoundedBuffer buffer;
    private List<String> textToWrite;

    /**
     * Constructor that initialize the buffer instance and the List instance
     * @param buffer    BoundedBuffer that monitors the Threads
     * @param text      List containing the String words
     */
    public Writer(BoundedBuffer buffer, List<String> text) {
        this.buffer = buffer;
        this.textToWrite = text;
    }

    /**
     * Threads run method that loops through the list and writes each
     * String to the buffer
     */
    public void run() {
        try {
            // Writes to the buffer
            for (int i = 0; i < textToWrite.size(); i++) {
                buffer.writeData(textToWrite.get(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
