package view;


import config.ClientConnection;

import java.util.Scanner;

import static config.ClientConnection.showText;

public class ClientChat {
    public static void main(String[] args) {
        ClientConnection client = new ClientConnection();
        Scanner scanner = new Scanner(System.in);
        showText("Write the IP [localhost by default]: ");
        String ip = scanner.nextLine();
        if (ip.length() <= 0) ip = "localhost";
        showText("Port [5050 by default]: ");
        String port = scanner.nextLine();
        if (port.length() <= 0) port = "5050";
        client.executeConnection(ip, Integer.parseInt(port));
        showText("To finish the conversation, write 'exit()'");
        client.writeData();
    }
}
