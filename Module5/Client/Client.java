package ass5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that acts as a client in Server-Client chat
 */
public class Client {
    private GUIChat gui;
    private DataInputStream dis;
    private DataOutputStream dos;

    /**
     * Constructor for the Client
     * @param serverIP IP address for the Client
     * @param serverPort Port for the Client
     */
    public Client(String serverIP, int serverPort) {
        this.gui = new GUIChat(this);
        gui.Start();

        try {
            // Create a new Socket
            Socket socket = new Socket(InetAddress.getByName(serverIP), serverPort );

            // Create input and output streams
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());


            while (true) {
                try {
                    String message = dis.readUTF();
                    gui.log("Server: " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for sending the message to the Server
     * @param str String message
     */
    public void sendMsg(String str) {
        try {
            dos.writeUTF(str);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}