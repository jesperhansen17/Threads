package assignment2;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Created by Jesper Hansen on 2015-11-19.
 */
public class Reader {

    // Inner class that extends a thread and sends data to the buffer syncronized with the reader
    public static class SyncReader extends Thread {
        private String inputStr;
        private JTextArea listR;
        private JLabel lblRec;
        private JPanel pnlColor;
        private CharacterBuffer buffer;
        private JLabel lblStatus;
        private Semaphore semaphore1;
        private Semaphore semaphore2;

        // The constructor to the SyncReader
        public SyncReader(String inputStr, JTextArea listR, JLabel lblRec, JPanel pnlColor, CharacterBuffer buffer, JLabel lblStatus, Semaphore semaphore1, Semaphore semaphore2) {
            this.inputStr = inputStr;
            this.listR = listR;
            this.lblRec = lblRec;
            this.pnlColor = pnlColor;
            this.buffer = buffer;
            this.lblStatus = lblStatus;
            this.semaphore1 = semaphore1;
            this.semaphore2 = semaphore2;
        }

        // Run method that loops as long there a character in the input string.
        // The method waits for the semaphore from the Writer that tells the
        // reader that new data has been writen to the buffer.
        public void run() {
            String value = "";
            int i = 0;
            while (i < inputStr.length()) {
                try {
                    // Lock readerThread, wait for semaphore1.release.
                    // Lock will be realesed when Writer is done writing to buffer.
                    semaphore1.acquire();

                    value += buffer.getBufferValue();
                    listR.append("Reading: " + buffer.getBufferValue() + "\n");
                    i++;

                    // Realese the lock for semaphore2, give the semaphore2 to Writer.
                    // Writer can begin to write to buffer again.
                    semaphore2.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lblRec.setText(value);
            // If the retrieved String is equal as the input string, let the user
            // know that sending went correctly
            if (inputStr.equals(value)) {
                System.out.println("Reader done");
                lblStatus.setText("SUCCESS");
                pnlColor.setBackground(Color.GREEN);
            } else {
                System.out.println("Reader not done");
                lblStatus.setText("NO SUCCESS");
                pnlColor.setBackground(Color.RED);
            }
        }
    }

    // Inner class that extends a thread and sends data to the buffer syncronized with the reader
    public static class AsyncReader extends Thread {
        private CharacterBuffer buffer;
        private JTextArea listR;
        private JLabel lblRec;
        private JLabel lblStatus;
        private String inputString;
        private JPanel pnlColor;
        private Random rand;

        public AsyncReader(JTextArea listR, JLabel lblRec, String inputString, CharacterBuffer buffer, JLabel lblStatus, JPanel pnlColor) {
            this.listR = listR;
            this.lblRec = lblRec;
            this.lblStatus = lblStatus;
            this.inputString = inputString;
            this.buffer = buffer;
            this.pnlColor = pnlColor;
            this.rand = new Random();
        }

        // Run method that loops as long there a character in the input string.
        // The method does not wait for the semaphore before reading the data.
        // Because of that errors can occur
        public void run() {
            String value = "";
            int i = 0;
            while (i < inputString.length()) {
                value += buffer.getBufferValue();
                listR.append("Reading: " + buffer.getBufferValue() + "\n");
                i++;
                try {
                    Thread.sleep(rand.nextInt(150));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lblRec.setText(value);
            // If the retrieved String is equal as the input string, let the user
            // know that sending went correctly
            if (inputString.equals(value)) {
                System.out.println("Reader done");
                lblStatus.setText("SUCCESS");
                pnlColor.setBackground(Color.GREEN);
            } else {
                System.out.println("Reader not done");
                lblStatus.setText("NO SUCCESS");
                pnlColor.setBackground(Color.RED);
            }

        }
    }
}
