package server;

import bb8uploader.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Integer port = new Integer(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Listening...");
            while(true) {
                Socket clientSocket = serverSocket.accept();
                if (clientSocket != null) {
                    Server server = new Server(clientSocket);
                    server.listen();
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
