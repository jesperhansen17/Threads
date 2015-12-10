package ass4;

import java.util.List;

/**
 * Class that extends Thread and reades String data to the BoundedBuffer
 */
public class Reader extends Thread {
    private BoundedBuffer buffer;
    private int count;
    private Controller controller;

    /**
     * Constructor that get a reference to the buffer and controller and get the number of Strings in the list
     * @param buffer        Bounded buffer that monitors the Thread
     * @param nbrOfStrings  Number of Strings in the list
     * @param controller    Controller that controlls the application
     */
    public Reader(BoundedBuffer buffer, int nbrOfStrings, Controller controller) {
        this.buffer = buffer;
        count = nbrOfStrings;
        this.controller = controller;
    }

    /**
     * Run method that reads the String from the buffer and appends it to a
     * StringBuffer. After the loop the StringBuffer is printed on the DestinationPane
     */
    public void run() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < count; i++) {
            try {
                sb.append(buffer.readData());
                sb.append(" ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        controller.setWord(sb.toString());
        controller.setElements(buffer.getNbrOfElements());
        buffer.markDest();
    }
}
