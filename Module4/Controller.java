package ass4;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private GUIMonitor gui;
    private BoundedBuffer boundedBuffer;
    public Writer writer;
    public Reader reader;
    public Modifier modifier;

    private List<String> wordList;

    /**
     * Constructor for the controller class. Starts the graphical interfaces and the connects the GUI with
     * the buffer and the threads
     */
    public Controller() {
        this.gui = new GUIMonitor(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.Start();
            }
        });
    }

    /**
     * Creates the BoundBuffer
     * @param sourcePane    SourcePane
     * @param destPane      DestinationPane
     * @param string        String that holds all of the text from the txt file
     * @param find          The word the user want to find
     * @param replace       The word thu user want to replace the found word with
     */
    public void createBuffer(JTextPane sourcePane, JTextPane destPane, String string, String find, String replace) {
        this.boundedBuffer = new BoundedBuffer(15, sourcePane, destPane, find, replace);

        wordList = new ArrayList<String>();

        for (String eachWord: string.split(" ")) {
            wordList.add(eachWord);
        }
    }

    /**
     * Method starts the Writer thread
     */
    public void startWriter() {
        writer = new Writer(boundedBuffer, wordList);
        writer.start();
    }

    /**
     * Method starts the Reader thread
     */
    public void startReader() {
        reader = new Reader(boundedBuffer, wordList.size(), this);
        reader.start();
    }

    /**
     * Method starts the Modifier thread
     */
    public void startModifier() {
        modifier = new Modifier(boundedBuffer, wordList.size());
        modifier.start();
    }

    /**
     * Set the copy String to the DestinationPane
     * @param string    The string containing the copy
     */
    public void setWord(String string) {
        gui.setDestination(string);
    }

    /**
     * Set the number of found element in the source text to the GUI
     * @param i The number of found elements
     */
    public void setElements(int i) {
        gui.setNbrElements(i);
    }
}
