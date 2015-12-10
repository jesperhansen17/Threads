
package ass4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import javax.swing.*;

/**
 * The GUI for assignment 4
 */
public class GUIMonitor
{
    /**
     * These are the components you need to handle.
     * You have to add listeners and/or code
     */
    private JFrame frame;				// The Main window
    private JMenu fileMenu;				// The menu
    private JMenuItem openItem;			// File - open
    private JMenuItem saveItem;			// File - save as
    private JMenuItem exitItem;			// File - exit
    private JTextField txtFind;			// Input string to find
    private JTextField txtReplace; 		// Input string to replace
    private JCheckBox chkNotify;		// User notification choise
    private JLabel lblInfo;				// Hidden after file selected
    private JButton btnCreate;			// Start copying
    private JButton btnClear;			// Removes dest. file and removes marks
    private JLabel lblChanges;			// Label telling number of replacements

    private JTextPane txtPaneSource;
    private JTextPane txtPaneDest;
    private String txtFile;
    private String finalTxtFile;

    private Controller controller;
    /**
     * Constructor
     */
    public GUIMonitor(Controller controller)
    {
        this.controller = controller;
    }

    /**
     * Starts the application
     */
    public void Start()
    {
        frame = new JFrame();
        frame.setBounds(0, 0, 714,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setTitle("Text File Copier - with Find and Replace");
        InitializeGUI();					// Fill in components
        frame.setVisible(true);
        frame.setResizable(false);			// Prevent user from change size
        frame.setLocationRelativeTo(null);	// Start middle screen
    }

    /**
     * Sets up the GUI with components
     */
    private void InitializeGUI()
    {
        fileMenu = new JMenu("File");

        openItem = new JMenuItem("Open Source File");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openItem.addActionListener(new ClickListener());

        saveItem = new JMenuItem("Save Destination File As sourceCopy.txt");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveItem.addActionListener(new ClickListener());

        saveItem.setEnabled(false);
        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        JMenuBar  bar = new JMenuBar();
        frame.setJMenuBar(bar);
        bar.add(fileMenu);

        JPanel pnlFind = new JPanel();
        pnlFind.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Find and Replace"));
        pnlFind.setBounds(12, 32, 436, 122);
        pnlFind.setLayout(null);
        frame.add(pnlFind);
        JLabel lab1 = new JLabel("Find:");
        lab1.setBounds(7, 30, 80, 13);
        pnlFind.add(lab1);
        JLabel lab2 = new JLabel("Replace with:");
        lab2.setBounds(7, 63, 80, 13);
        pnlFind.add(lab2);

        txtFind = new JTextField();
        txtFind.setBounds(88, 23, 327, 20);
        pnlFind.add(txtFind);
        txtReplace = new JTextField();
        txtReplace.setBounds(88, 60, 327, 20);
        pnlFind.add(txtReplace);
        chkNotify = new JCheckBox("Notify user on every match");
        chkNotify.setBounds(88, 87, 180, 17);
        pnlFind.add(chkNotify);

        lblInfo = new JLabel("Select Source File..");
        lblInfo.setBounds(485, 42, 120, 13);
        frame.add(lblInfo);

        btnCreate = new JButton("Copy to Destination");
        btnCreate.setBounds(465, 119, 230, 23);
        btnCreate.addActionListener(new ClickListener());

        frame.add(btnCreate);
        btnClear = new JButton("Clear dest. and remove marks");
        btnClear.setBounds(465, 151, 230, 23);
        frame.add(btnClear);
        btnClear.addActionListener(new ClickListener());

        lblChanges = new JLabel("No. of Replacements:");
        lblChanges.setBounds(279, 161, 200, 13);
        frame.add(lblChanges);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(12, 170, 653, 359);
        frame.add(tabbedPane);
        txtPaneSource = new JTextPane();
        JScrollPane scrollSource = new JScrollPane(txtPaneSource);
        tabbedPane.addTab("Source", null, scrollSource, null);
        txtPaneDest = new JTextPane();
        JScrollPane scrollDest = new JScrollPane(txtPaneDest);
        tabbedPane.addTab("Destination", null, scrollDest, null);
    }

    /**
     * Reads a text file from the harddrive
     * @return  String containing the text from the file
     */
    public String readFile() {
        String everything = "";
        JFileChooser jf = new JFileChooser();

        int returnval = jf.showOpenDialog(frame);

        if (returnval == JFileChooser.APPROVE_OPTION) {
            File file = jf.getSelectedFile();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                everything = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return everything;
    }

    /**
     * Save the copy String to a text file
     */
    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("sourceCopy.txt"));
            out.write(finalTxtFile);  //Replace with the string
            //you are trying to write
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("Exception ");

        }
    }

    /**
     * Prints the String to the Destination Pane
     * @param string The printed String
     */
    public void setDestination(String string) {
        finalTxtFile = string;
        txtPaneDest.setText(string);
    }

    /**
     * Prints the number of found elements to the GUI
     * @param i Number of found elements
     */
    public void setNbrElements(int i) {
        lblChanges.setText("No. of Replacements: " + i);
    }

    /**
     * Inner class that handles the button clicks from the Graphical User Interface
     */
    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(openItem)) {
                txtFile = readFile();
                txtPaneSource.setText(txtFile);
            } else if (e.getSource().equals(saveItem)) {
                saveFile();
            } else if (e.getSource().equals(btnCreate)) {
                if (chkNotify.isSelected()) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Replace " + txtFind.getText() + " with " + txtReplace.getText() + "?","Warning", dialogButton);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        controller.createBuffer(txtPaneSource, txtPaneDest, txtFile, txtFind.getText(), txtReplace.getText());
                        controller.startWriter();
                        controller.startReader();
                        controller.startModifier();;
                    }
                } else {
                    controller.createBuffer(txtPaneSource, txtPaneDest, txtFile, txtFind.getText(), txtReplace.getText());
                    controller.startWriter();
                    controller.startReader();
                    controller.startModifier();
                }
                saveItem.setEnabled(true);
            } else if (e.getSource().equals(btnClear)) {
                txtPaneDest.setText("");
                txtPaneSource.setText("");
                saveItem.setEnabled(false);
                txtFind.setText("");
                txtReplace.setText("");
                chkNotify.setSelected(false);
            }
        }
    }
}
