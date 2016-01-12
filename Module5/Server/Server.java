package ass5;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class for the Chat application
 */
public class Server {
    private GUIChat gui;
    private ExecutorService executorService;

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    // This server can accept up to maxClientCount's connections
    private static final int maxClientCount = 5;

    private List<Client> clientList;

    /**
     * Constructor for the Server class
     */
    public Server() {
        gui = new GUIChat(this);
        gui.Start();

        clientList = new ArrayList<Client>();

        // Create a threadPool that handles 5 different threads
        // that can handle up to 5 different clients
        executorService = Executors.newFixedThreadPool(maxClientCount);

        int portNumber = 50000;
        System.out.println("Now using portnumber: " + portNumber);

        /*
         * Open a server socket on the portnumber: 50000
         */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        /*
         * Create a client socket for each connection and pass it to a new client thread.
         */
        gui.log("Waiting for users....");
        while (true) {
            try {
                clientSocket = serverSocket.accept();

                Client client = new Client(clientSocket, this);
                executorService.execute(client);
                clientList.add(client);
                System.out.println(clientList.size());

                // Create a new thread that checks for msg
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void readMsg() {
        Runnable readMsg = () -> {
            
        };
    }

    /**
     * Method for sending a message with the outputStream
     * @param str String that is about to be send
     */
    public void sendMsg(String str) {
        try {
            DataOutputStream tempDos;
            for (int i = 0; i < clientList.size(); i++) {
                tempDos = clientList.get(i).getDos();
                tempDos.writeUTF(str);
                tempDos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the client from the list
     * @param client The client that is about to be removed
     */
    public void cancelClient(Client client) {
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).equals(client)) {
                gui.log(clientList.get(i).getName() + " left the chat");
                clientList.remove(i);
            }
        }
    }

    /**
     * Updates the GUI
     * @param str
     */
    public void updateGui(String str) {
        gui.log(str);
    }
}