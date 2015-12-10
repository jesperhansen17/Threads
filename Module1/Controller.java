package guis;

import javax.swing.*;

// Public class that controls the program
public class Controller {
    private GUIFrame gui;
    private MusicPlayer musicPlayer;
    private ThreadController threadController;

    // Constructor initialize all class objects
    public Controller() {
        gui = new GUIFrame(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.Start();
            }
        });
        musicPlayer = new MusicPlayer();
        threadController = new ThreadController();
    }

    // Handles all the actions that the user can do
    public void userAction(Actions.UserActions action) {
        switch (action) {
            case OPENFOLDER:
                gui.getLblPlayURL().setText("Choosen song: " + musicPlayer.openFile(gui.getFolderFrame()));
                break;
            case PLAYMUSIC:
                musicPlayer.startMusic();
                break;
            case STOPMUSIC:
                musicPlayer.stopMusic();
                break;
            case MOVELABEL:
                threadController.startMovingLabel(gui.getLblMove(), gui.getPnlMove());
                break;
            case STOPLABEL:
                threadController.stopMovingLabel();
                break;
            case STARTCLOCK:
                threadController.startClock(gui.getLblClock(), gui.getPnlRotate());
                break;
            case STOPCLOCK:
                threadController.stopClock();
                break;
        }
    }
}
