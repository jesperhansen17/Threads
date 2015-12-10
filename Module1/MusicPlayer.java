package guis;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 * Created by JesperHansen on 2015-11-12.
 */
public class MusicPlayer {
    private File musicFile;                     // File contains the wav or mp3 file
    private JFileChooser fc;                    // FileChooser to retreive the MP3 file
    private boolean musicIsPlaying = false;     // MusicIsNotPlaying

    private InputStream inputStream;
    private AudioStream audioStream;

    public MusicPlayer() {
        // Initialize the JFileChooser
        fc = new JFileChooser();
        setupFilter();
    }

    /**
     * Method for retreiving the Mp3 file
     * @param frame JFrame for open the Dialog to retreive the MP3 file
     * @return String with the name of the MP3 file
     */
    public String openFile(JFrame frame) {
        int returnVal = fc.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            musicFile = fc.getSelectedFile();
            setupMusic();
            return musicFile.getName();
        } else {
            return "";
        }
    }

    /**
     * Set up a JFileChooser filter for only mp3 and wav files
     */
    public void setupFilter() {
        fc.addChoosableFileFilter(new FileNameExtensionFilter("*wav", "wav"));
        fc.setAcceptAllFileFilterUsed(true);
    }

    /**
     * Mehods sets up the the MusicPlayer with an mp3 or wav file
     */
    private void setupMusic() {
        try {
            inputStream = new FileInputStream(musicFile.getPath());
            audioStream = new AudioStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Starts the music
     */
    public void startMusic() {
        if (!musicIsPlaying && musicFile != null) {
            AudioPlayer.player.start(audioStream);
            musicIsPlaying = true;
        }
    }

    /**
     * Stops the music
     */
    public void stopMusic() {
        if (musicIsPlaying) {
            AudioPlayer.player.stop(audioStream);
            musicIsPlaying = false;
        }
    }
}
