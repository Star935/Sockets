package view;

import config.ServerConnection;

import java.util.Scanner;

import static config.ServerConnection.showText;

public class ServerChat {
    public static void main(String[] args) {
        ServerConnection s = new ServerConnection();
        Scanner sc = new Scanner(System.in);
        showText("Write the port [5050 by default]: ");
        String port = sc.nextLine();
        if (port.length() <= 0) port = "5050";
        s.executeConnection(Integer.parseInt(port));
        showText("To close the server, write 'exit()'");
        s.writeData();
    }
}
