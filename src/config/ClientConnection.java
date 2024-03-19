package config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection {
    private Socket socket;
    private DataInputStream inputData = null; /*We define both input and output (below) methods to write the data
    in binary, which can be sent either to a file or through a network.*/
    private DataOutputStream outputData = null;
    Scanner scanner = new Scanner(System.in);
    final String TERMINATION_COMMAND = "exit()";

    public void openConnection(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            showText("Connected to :" + socket.getInetAddress().getHostName());
        } catch (Exception e) {
            showText("Exception in openConnection(): " + e.getMessage());
            System.exit(0);
        }
    }

    public static void showText(String s) {
        System.out.println(s);
    }

    public void openFlows() {
        try {
            inputData = new DataInputStream(socket.getInputStream());
            outputData = new DataOutputStream(socket.getOutputStream());
            outputData.flush();
        } catch (IOException e) {
            showText("Exception on openFlows()");
        }
    }

    public void send(String s) {
        try {
            //Encode the string using UTF-8 format and write it to the output stream
            outputData.writeUTF(s);
            outputData.flush();
        } catch (IOException e) {
            showText("IOException on send()");
        }
    }

    public void closeConnection() {
        try {
            inputData.close();
            outputData.close();
            socket.close();
            showText("Connection finished");
        } catch (IOException e) {
            showText("IOException on closeConnection()");
        }finally{
            System.exit(0);
        }
    }

    public void executeConnection(String ip, int port) {
        Thread thread = new Thread(() -> {
            try {
                openConnection(ip, port);
                openFlows();
                receiveData();
            } finally {
                closeConnection();
            }
        });
        thread.start();
    }

    public void receiveData() {
        String st = "";
        try {
            do {
                st = inputData.readUTF();
                showText("\n[Server] => " + st);
                System.out.print("\n[You] => ");
            } while (!st.equals(TERMINATION_COMMAND));
        } catch (IOException e) {}
    }

    public void writeData() {
        String input = "";
        while (true) {
            System.out.print("[You] => ");
            input = scanner.nextLine();
            if(input.length() > 0)
                send(input);
        }
    }
}
