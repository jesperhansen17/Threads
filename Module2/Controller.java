package assignment2;

import javax.swing.*;
import java.util.concurrent.Semaphore;

/**
 * Created by Jesper Hansen on 2015-11-16.
 */
public class Controller {
    private GUIMutex gui;
    private CharacterBuffer buffer;
    private Semaphore semaphore1;
    private Semaphore semaphore2;

    public Controller() {
        gui = new GUIMutex(this);
        buffer = new CharacterBuffer();
        semaphore1 = new Semaphore(0);
        semaphore2 = new Semaphore(0);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.Start();
            }
        });
    }

    // Method that creates and starts two threads, these two threads are working synchronized
    public void SyncMode(String inputStr, JTextArea listR, JTextArea listW, JLabel lblRec, JPanel pnlColor, JLabel lblStatus) {
        new Writer.SyncWriter(inputStr,listW, buffer, semaphore1, semaphore2).start();
        new Reader.SyncReader(inputStr, listR, lblRec, pnlColor, buffer, lblStatus, semaphore1, semaphore2).start();
    }

    // Method that creates and starts two threads, these two threads are working asynchronized
    public void AsyncMode(String inputStr, JTextArea listR, JTextArea listW, JLabel lblRec, JPanel pnlColor, JLabel lblStatus) {
        new Writer.AsyncWriter(inputStr, listW, buffer).start();
        new Reader.AsyncReader(listR, lblRec, inputStr, buffer, lblStatus, pnlColor).start();
    }
}
