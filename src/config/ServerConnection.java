package config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream inputData = null;
    private DataOutputStream outputData = null;
    Scanner scanner = new Scanner(System.in);
    final String TERMINATION_COMMAND = "exit()";

    public void openConnection(int port) {
        try {
            serverSocket = new ServerSocket(port);
            showText("Waiting for connection in the port " + String.valueOf(port) + "...");
            socket = serverSocket.accept(); //Waits for a connection to the socket to accept it

            //Returns the address and host name which the socket is connected
            showText("Connected to: " + socket.getInetAddress().getHostName() + "\n\n\n");
        } catch (Exception e) {
            showText("Exception on openConnection(): " + e.getMessage());
            System.exit(0);
        }
    }
    public void flows() {
        try {
            inputData = new DataInputStream(socket.getInputStream());
            outputData = new DataOutputStream(socket.getOutputStream());

            //Send all the data within the buffer to the network
            outputData.flush();
        } catch (IOException e) {
            showText("Exception on flows()");
        }
    }

    public void receiveData() {
        String st = "";
        try {
            do {
                //Read the string in the utf format
                st = inputData.readUTF();
                showText("\n[Client] => " + st);
                System.out.print("\n[You] => ");
            } while (!st.equals(TERMINATION_COMMAND));
        } catch (IOException e) {
            closeConnection();
        }
    }


    public void send(String s) {
        try {
            outputData.writeUTF(s);
            outputData.flush();
        } catch (IOException e) {
            showText("Exception on send(): " + e.getMessage());
        }
    }

    public static void showText(String s) {
        System.out.print(s);
    }

    public void writeData() {
        while (true) {
            System.out.print("\n [You] => ");
            send(scanner.nextLine());
        }
    }

    public void closeConnection() {
        try {
            inputData.close();
            outputData.close();
            socket.close();
        } catch (IOException e) {
            showText("Exception on closeConnection(): " + e.getMessage());
        } finally {
            showText("Conversation finished....");
            System.exit(0);

        }
    }

    public void executeConnection(int port) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    openConnection(port);
                    flows();
                    receiveData();
                } finally {
                    closeConnection();
                }
            }
        });
        thread.start();
    }
}
