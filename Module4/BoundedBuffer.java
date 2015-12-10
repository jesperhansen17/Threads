package ass4;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Jesper Hansen on 2015-12-08.
 */

/**
 * Enumerator that holds the StatusBuffers status
 */
enum BufferStatus { EMPTY, CHECKED, OCCUPIED }

public class BoundedBuffer {
    private String[] strArr;            // The actual String buffer
    private BufferStatus[] status;      // An array of BufferStatus objects, one for each element in buffer
    private final int MAX;              // Elements in buffer

    private int writePos;               // The positions pointers for each thread
    private int readPos;
    private int findPos;

    private String findString;          // The String to search for
    private String replaceString;       // The replaceString

    private JTextPane copyPane;
    private JTextPane destPane;

    private int nbrReplacements;        // Replacement counter

    private final Lock lock = new ReentrantLock();
    private final Condition EMPTY = lock.newCondition();
    private final Condition CHECKED = lock.newCondition();
    private final Condition OCCUPIED = lock.newCondition();

    /**
     * Constructor that initialize the BoundedBuffer
     * @param elements  Number of elements that the BoundedBuffer can hold
     * @param copyPane  JTextPane that prints the source text
     * @param destPane  JTextPane that prints the copy text
     * @param find      The String that the user wants to replace
     * @param replace   The String that the user is replacing the original with
     */
    public BoundedBuffer(int elements, JTextPane copyPane, JTextPane destPane, String find, String replace) {
        this.copyPane = copyPane;
        this.destPane = destPane;
        this.MAX = elements;
        this.findString = find;
        this.replaceString = replace;

        strArr = new String[MAX];
        status = new BufferStatus[MAX];
        writePos = 0; readPos = 0; findPos = 0;

        // Fills statusBuffer with the status EMPTY
        for (int i = 0; i < status.length; i++) {
            status[i] = BufferStatus.EMPTY;
        }

        // Marks the word the user wants to replace
        markSource();
    }

    /**
     * Method that look at the added String and replaces it if the String is the
     * searched String. Method is awaits for the signal from the writeData() method before
     * it can continue its process
     * @throws InterruptedException Throws exception if it is Interrupted
     */
    public void modify() throws InterruptedException {
        lock.lock();

        try {
            // Wait here if the poistion in the array is not OCCUPIED
            while (!(status[findPos].equals(BufferStatus.OCCUPIED))) {
                OCCUPIED.await();
            }

            // Don't replace anything if the textAreas are empty
            if (!(findString.isEmpty() ||replaceString.isEmpty())) {
                strArr[findPos] = strArr[findPos].replace(findString, replaceString);
            }

            // Change Status to CHECKED
            status[findPos] = BufferStatus.CHECKED;
            findPos = ((findPos + 1) % MAX);        // Increment the findPos
            CHECKED.signal();                       // Signal that CHECKED can work now
        } finally {
            lock.unlock();
        }
    }

    /**
     * Method that reads the String data from the StringArray.
     * Method is awaits for the signal from the modify() method before
     * it can continue its process
     * @return The String from the Buffer at pos readPos
     * @throws InterruptedException Throws exception if it is Interrupted
     */
    public String readData() throws InterruptedException {
        lock.lock();
        try {
            // Wait here if the poistion in the array is not CHECKED
            while (!(status[readPos].equals(BufferStatus.CHECKED))) {
                CHECKED.await();
            }

            // Read the String from the Array
            String word = "";
            word =  strArr[readPos];

            // Update Status to EMPTY
            status[readPos] = BufferStatus.EMPTY;
            readPos = ((readPos + 1) % MAX);
            EMPTY.signal();         // Signal that EMPTY can work now

            return word;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Method that writes data to the Buffer.
     * Method is awaits for the signal from the readData() method before
     * it can continue its process
     * @param s The String that the method will write to the Buffer
     * @throws InterruptedException Throws exception if it is Interrupted
     */
    public void writeData(String s) throws InterruptedException{
        lock.lock();
        try {
            // Wait here if the poistion in the array is not EMPTY
            while (!(status[writePos].equals(BufferStatus.EMPTY))) {
                EMPTY.await();
            }
            // Write the String s to the Array
            strArr[writePos] = s;

            // Update Status to OCCUPIED and increment writPos
            status[writePos] = BufferStatus.OCCUPIED;
            writePos = ((writePos + 1) % MAX);

            OCCUPIED.signal();
        } finally {
            lock.unlock();
        }
    }


    /**
     * Marks the findString in the SourcePane
     */
    public void markSource()
    {
        /**
         * A private subclass of the default highlight painter
         */
        class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
        {
            public MyHighlightPainter(Color color)
            {
                super(color);
            }
        }

        // An instance of the private subclass of the default highlight painter
        Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.green);

        try
        {
            if (!(findString.isEmpty() ||replaceString.isEmpty())) {
                Highlighter hilite = copyPane.getHighlighter();
                Document doc = copyPane.getDocument();
                String text = doc.getText(0, doc.getLength());
                int pos = 0;

                // Search for pattern
                // see I have updated now its not case sensitive
                while ((pos = text.indexOf(findString, pos)) >= 0)
                {
                    // Create highlighter using private painter and apply around pattern
                    hilite.addHighlight(pos, pos+findString.length(), myHighlightPainter);
                    pos += findString.length();
                    nbrReplacements++;
                }
            }
        } catch (BadLocationException e) {
        }
    }

    /**
     * Marks the replaceString in the DestinationPane
     */
    public void markDest()
    {
        /**
         * A private subclass of the default highlight painter
         */
        class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
        {
            public MyHighlightPainter(Color color)
            {
                super(color);
            }
        }

        // An instance of the private subclass of the default highlight painter
        Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);

        try
        {
            if (!(findString.isEmpty() ||replaceString.isEmpty())) {
                Highlighter hilite = destPane.getHighlighter();
                Document doc = destPane.getDocument();
                String text = doc.getText(0, doc.getLength());
                int pos = 0;

                // Search for pattern
                // see I have updated now its not case sensitive
                while ((pos = text.indexOf(replaceString, pos)) >= 0)
                {
                    // Create highlighter using private painter and apply around pattern
                    hilite.addHighlight(pos, pos+replaceString.length(), myHighlightPainter);
                    pos += replaceString.length();
                }
            }
        } catch (BadLocationException e) {
        }
    }

    /**
     * Method returns the number of replacements that need to be done
     * @return  int Number of replacements
     */
    public int getNbrOfElements() {
        return nbrReplacements;
    }
}
