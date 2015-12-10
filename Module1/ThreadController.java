package guis;

import javax.swing.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// Public class that controls the two different threads
public class ThreadController {
    private volatile boolean bThreadExit;
    private volatile boolean bThreadRunning = false;
    private volatile boolean bClockExit;
    private volatile boolean bClockRunning = false;

    public ThreadController() {

    }

    // Method for starting and controlling the Thread that makes the JLabel to "jump"
    public void startMovingLabel(JLabel lblThread, JPanel pnlMove) {
        bThreadExit = false;

        Runnable task1 = () -> {                        // Create a new Runnable
            Random rand = new Random();
            String name = Thread.currentThread().getName();
            lblThread.setText(name);
            try {
                while(!bThreadExit) {                   // Runs until the flag bThreadExit sets to true
                    SwingUtilities.invokeLater(() -> {
                        lblThread.setLocation(rand.nextInt(100), rand.nextInt(175));
                        pnlMove.revalidate();
                        pnlMove.repaint();
                    });

                    System.out.println(Thread.currentThread().getName());
                    TimeUnit.MILLISECONDS.sleep(500);   // Sleep the thread fo 0.5 seconds
                }
                lblThread.setText("");
                bThreadRunning = false;                 // Set false to the flag that checks if an thread is already running
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // Start a new thread only if a thread is not currently running
        if (!bThreadRunning) {
            Thread thread1 = new Thread(task1);
            thread1.start();
            bThreadRunning = true;
        }
    }

    /**
     * Method that stops the "jumping" JLabel by setting bThreadExit to true
     */
    public void stopMovingLabel() {
        bThreadExit = true;
    }

    /**
     * Method for starting and controlling the thread controls the clock
     * @param lblClock JLabel for the clock
     * @param pnlRotate JLabel for the JPanel that holds the lblClock
     */
    public void startClock(JLabel lblClock, JPanel pnlRotate) {
        bClockExit = false;
        Runnable task2 = () -> {                // Create a new Runnable
            try {
                while (!bClockExit) {           // Runs until bClockExit sets to true
                    System.out.println(Thread.currentThread().getName());
                    SwingUtilities.invokeLater(() -> {
                        lblClock.setText(updateClock());
                        pnlRotate.revalidate();
                        pnlRotate.repaint();
                    });
                    TimeUnit.SECONDS.sleep(1);  // Sleep for 1 second
                }
                lblClock.setText("");
                bClockRunning = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        // Start a new thread only if a thread is not currently running
        if (!bClockRunning) {
            Thread thread2 = new Thread(task2);
            thread2.start();
            bClockRunning = true;
        }

    }

    /**
     *  Method stops the clock thread
     */
    public void stopClock() {
        bClockExit = true;
    }

    /**
     * Private metohod retrives an Calender and converts it to an String
     * @return String
     */
    private String updateClock() {
        Calendar calendar = new GregorianCalendar();
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String seconds = String.valueOf(calendar.get(Calendar.SECOND));
        return (hour + ":" + minute + ":" + seconds);
    }
}
