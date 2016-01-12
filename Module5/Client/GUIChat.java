package ass5;

/**
 * Created by JesperHansen on 2016-01-08.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The GUI for assignment 5
 */
public class GUIChat
{
    /**
     * These are the components you need to handle.
     * You have to add listeners and/or code
     */
    private JFrame frame;				// The Main window
    private JTextField txt;				// Input for text to send
    private JButton btnSend;			// Send text in txt
    private JTextArea lstMsg;			// The logger listbox
    private StringBuilder sb;
    private Client client;
    /**
     * Constructor
     */
    public GUIChat(Client client)
    {
        this.client = client;
        sb = new StringBuilder();
    }

    /**
     * Starts the application
     */
    public void Start()
    {
        frame = new JFrame();
        frame.setBounds(100, 100, 300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setTitle("Multi Chat Client");			// Change to "Multi Chat Server" on server part and vice versa
        InitializeGUI();					// Fill in components
        frame.setVisible(true);
        frame.setResizable(false);			// Prevent user from change size
        addListener();
    }

    /**
     * Sets up the GUI with components
     */
    private void InitializeGUI()
    {
        txt = new JTextField();
        txt.setBounds(13,  13, 177, 23);
        frame.add(txt);
        btnSend = new JButton("Send");
        btnSend.setBounds(197, 13, 75, 23);
        frame.add(btnSend);
        lstMsg = new JTextArea();
        lstMsg.setEditable(false);
        JScrollPane pane = new JScrollPane(lstMsg);
        pane.setBounds(12, 51, 260, 199);
        pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(pane);
    }

    /**
     * Private method for handling the listeners from the GUI
     */
    private void addListener() {
        btnSend.addActionListener(new ButtonListener());
    }

    /**
     * Private inner class that implements an ActionListener and handles listener from
     * the send button.
     */
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(btnSend)) {
                String str = txt.getText().toString();
                txt.setText("");
                client.sendMsg(str);
            }
        }
    }

    /**
     * Method for printing out a String on the GUI
     * @param str The String that is about to be printed
     */
    public void log(String str) {
        sb.append(str + "\n");
        lstMsg.setText(sb.toString());
    }
}

