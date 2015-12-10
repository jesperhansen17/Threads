package assignment2;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Created by JesperHansen on 2015-11-19.
 */
public class Writer {
    // Inner class that extends a thread and sends data to the buffer syncronized with the reader
    public static class SyncWriter extends Thread {
        private CharacterBuffer buffer;
        private JTextArea listW;
        private String inputString;
        private Semaphore semaphore1;
        private Semaphore semaphore2;
        private Random rand;

        public SyncWriter(String inputString, JTextArea listW, CharacterBuffer buffer, Semaphore semaphore1, Semaphore semaphore2) {
            this.listW = listW;
            this.inputString = inputString;
            this.buffer = buffer;
            this.semaphore1 = semaphore1;
            this.semaphore2 = semaphore2;
            this.rand = new Random();
        }

        // The run method that loops through all the characters of the string and writes each character
        // to the buffer. When a character is added to the buffer it releases the semphore and let the
        // reader be able to read the character in the buffer
        public void run() {
            int i = 0;
            while (i < inputString.length()) {
                try {

                    buffer.setBufferValue(inputString.charAt(i));
                    listW.append("Writing: " + inputString.charAt(i) + "\n");
                    i++;

                    // release lock for semaphore1
                    // Reader can now read from the buffer and Writer stops after this
                    // and waits for the Reader to be done.
                    semaphore1.release();
                    // Wait here for semaphore2.release.
                    // Writer gets semaphore when Reader is done reading
                    semaphore2.acquire();
                    Thread.sleep(rand.nextInt(5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Writer done");
        }
    }

    // Inner class that extends a thread and sends data to the buffer asyncronized with the reader
    public static class AsyncWriter extends Thread {
        private CharacterBuffer buffer;
        private JTextArea listW;
        private String inputString;
        private Random rand;

        public AsyncWriter(String inputString, JTextArea listW, CharacterBuffer buffer) {
            this.listW = listW;
            this.inputString = inputString;
            this.buffer = buffer;
            this.rand = new Random();
        }

        // The run method that loops through all the characters of the string and writes each character
        // to the buffer. This run method runs as long there are no more characters from the the string.
        // Method does not wait tell the reader that new data has been written
        public void run() {
            int i = 0;
            while (i < inputString.length()) {
                try {
                    buffer.setBufferValue(inputString.charAt(i));
                    listW.append("Writing: " + inputString.charAt(i) + "\n");
                    i++;
                    Thread.sleep(rand.nextInt(150));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Writer done");
        }
    }
}
