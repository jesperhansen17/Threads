package ass5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Private inner class that implements a Runnable for handling the
 * startup connection with a client. And then receiving the message from
 * the client.
 */
public class Client implements Runnable {
    // Create instance variable for the input and output stream
    private DataInputStream dis;
    private DataOutputStream dos;
    private Server server;
    private String name;

    public Client(Socket socket, Server server) {
        this.server = server;
        /**
         * Create input and output
         */
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            /**
             * Get the users name
             */
            dos.writeUTF("Enter name for joining chat: ");
            dos.flush();
            name = dis.readUTF();
            server.updateGui(name + " joined chat");

            /**
             * Welcome the new client
             */
            dos.writeUTF("Welcome to the chat " + name);
            dos.flush();

            /**
             * Listen for messages
             */
            while (true) {
                String message = dis.readUTF();
                server.updateGui(name + ": " + message);
            }
        } catch (IOException e) {
            server.cancelClient(this);
            System.out.println(e);
        }
    }

    /**
     * Method for retriving the clients DataOutputStream
     * @return DataOutputStream
     */
    public DataOutputStream getDos() {
        return dos;
    }

    /**
     * Method for retriving the clients name
     * @return String name
     */
    public String getName() {
        return name;
    }
}
