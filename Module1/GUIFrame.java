
package guis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The GUI for assignment 1
 */
public class GUIFrame
{
    /**
     * These are the components you need to handle.
     * You have to add listeners and/or code
     */
    private JFrame frame;		// The Main window
    private JButton btnOpen;	// Open sound file button
    private JButton btnPlay;	// Play selected file button
    private JButton btnStop;	// Stop music button
    private JButton btnDisplay;	// Start thread moving display
    private JButton btnDStop;	// Stop moving display thread
    private JButton btnTriangle;// Start moving graphics thread
    private JButton btnTStop;	// Stop moving graphics thread
    private JLabel lblPlaying;	// Hidden, shown after start of music
    private JLabel lblPlayURL;	// The sound file path
    private JPanel pnlMove;		// The panel to move display in
    private JPanel pnlRotate;	// The panel to move graphics in
    private JLabel lblMove;     // The label with the moving name
    private JLabel lblClock;    // The label prints the current time

    // Class for controlling the GUI
    private Controller controller;

    /**
     * Constructor
     */
    public GUIFrame(Controller controller)
    {
        // Initialize the instance variable controller
        this.controller = controller;
    }

    /**
     * Starts the application
     */
    public void Start()
    {
        frame = new JFrame();
        frame.setBounds(0, 0, 494, 437);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setTitle("Multiple Thread Demonstrator");
        InitializeGUI();					// Fill in components
        frame.setVisible(true);
        frame.setResizable(false);			// Prevent user from change size
        frame.setLocationRelativeTo(null);	// Start middle screen

        btnListeners();
    }

    /**
     * Sets up the GUI with components
     */
    private void InitializeGUI()
    {
        // The play panel
        JPanel pnlSound = new JPanel();
        Border b1 = BorderFactory.createTitledBorder("Music Player");
        pnlSound.setBorder(b1);
        pnlSound.setBounds(12, 12, 450, 100);
        pnlSound.setLayout(null);

        // Add the buttons and labels to this panel
        btnOpen = new JButton("Open");
        btnOpen.setBounds(6, 71, 75, 23);
        pnlSound.add(btnOpen);

        btnPlay = new JButton("Play");
        btnPlay.setBounds(88, 71, 75, 23);
        pnlSound.add(btnPlay);

        btnStop = new JButton("Stop");
        btnStop.setBounds(169, 71, 75, 23);
        pnlSound.add(btnStop);

        lblPlaying = new JLabel("Now Playing...",JLabel.CENTER);
        lblPlaying.setFont(new Font("Serif", Font.BOLD, 20));
        lblPlaying.setBounds(128, 16, 120, 30);
        pnlSound.add(lblPlaying);

        lblPlayURL = new JLabel("Music url goes here");
        lblPlayURL.setBounds(10, 44, 400, 13);
        pnlSound.add(lblPlayURL);
        // Then add this to main window
        frame.add(pnlSound);

        // The moving display outer panel
        JPanel pnlDisplay = new JPanel();
        Border b2 = BorderFactory.createTitledBorder("Display Thread");
        pnlDisplay.setBorder(b2);
        pnlDisplay.setBounds(12, 118, 222, 269);
        pnlDisplay.setLayout(null);

        // Add buttons and drawing panel to this panel
        btnDisplay = new JButton("Start Display");
        btnDisplay.setBounds(10, 226, 121, 23);
        pnlDisplay.add(btnDisplay);

        btnDStop = new JButton("Stop");
        btnDStop.setBounds(135, 226, 75, 23);
        pnlDisplay.add(btnDStop);

        pnlMove = new JPanel();
        pnlMove.setBounds(10,  19,  200,  200);
        Border b21 = BorderFactory.createLineBorder(Color.black);
        pnlMove.setBorder(b21);
        pnlMove.setLayout(null);

        // Label holding the "jumping" thread name
        lblMove = new JLabel();
        lblMove.setBounds(0, 0, 100, 25);

        pnlMove.add(lblMove);

        pnlDisplay.add(pnlMove);

        // Then add this to main window
        frame.add(pnlDisplay);

        // The moving graphics outer panel
        JPanel pnlClock = new JPanel();
        Border b3 = BorderFactory.createTitledBorder("Triangle Thread");
        pnlClock.setBorder(b3);
        pnlClock.setBounds(240, 118, 222, 269);
        pnlClock.setLayout(null);

        // Add buttons and drawing panel to this panel
        btnTriangle = new JButton("Start Rotate");
        btnTriangle.setBounds(10, 226, 121, 23);
        pnlClock.add(btnTriangle);

        btnTStop = new JButton("Stop");
        btnTStop.setBounds(135, 226, 75, 23);
        pnlClock.add(btnTStop);

        // Label holding the clock
        lblClock = new JLabel();
        lblClock.setBounds(50, 50, 100, 23);

        pnlRotate = new JPanel();
        pnlRotate.setBounds(10,  19,  200,  200);
        Border b31 = BorderFactory.createLineBorder(Color.black);
        pnlRotate.setBorder(b31);
        pnlRotate.setLayout(null);
        pnlRotate.add(lblClock);
        pnlClock.add(pnlRotate);
        // Add this to main window
        frame.add(pnlClock);
    }

    //#########################################################################################
    // Getters so that the controller can retrieve and manipulate the different GUI containers
    //#########################################################################################
    public JFrame getFolderFrame() {
        return frame;
    }

    public JLabel getLblPlayURL() {
        return lblPlayURL;
    }

    public JPanel getPnlMove() {
        return pnlMove;
    }

    public JLabel getLblMove() {
        return lblMove;
    }

    public JPanel getPnlRotate() {
        return pnlRotate;
    }

    public JLabel getLblClock() {
        return lblClock;
    }

    // Private method for setting up Button Listeners
    private void btnListeners() {
        btnOpen.addActionListener(new BtnAction());
        btnPlay.addActionListener(new BtnAction());
        btnStop.addActionListener(new BtnAction());
        btnDisplay.addActionListener(new BtnAction());
        btnDStop.addActionListener(new BtnAction());
        btnTriangle.addActionListener(new BtnAction());
        btnTStop.addActionListener(new BtnAction());
    }

    // Private class that handles the different button clicks
    private class BtnAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(btnOpen)) {
                controller.userAction(Actions.UserActions.OPENFOLDER);
            } else if (e.getSource().equals(btnPlay)) {
                controller.userAction(Actions.UserActions.PLAYMUSIC);
            } else if (e.getSource().equals(btnStop)) {
                controller.userAction(Actions.UserActions.STOPMUSIC);
            } else if (e.getSource().equals(btnDisplay)) {
                controller.userAction(Actions.UserActions.MOVELABEL);
            } else if (e.getSource().equals(btnDStop)) {
                controller.userAction(Actions.UserActions.STOPLABEL);
            } else if (e.getSource().equals(btnTriangle)) {
                controller.userAction(Actions.UserActions.STARTCLOCK);
            } else if (e.getSource().equals(btnTStop)) {
                controller.userAction(Actions.UserActions.STOPCLOCK);
            }
        }
    }

}
