package client;

import bb8uploader.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String address = args[0];
        Integer port = new Integer(args[1]);

        try {
            Socket socket = new Socket(address, port);
            Client client = new Client(socket);
            String input;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            do {
                input = reader.readLine();
            } while (!client.parseInput(input));
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
