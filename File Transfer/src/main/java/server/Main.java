package server;

import bb8uploader.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Integer port = new Integer(args[0]);

        Server server = new Server(port);
    }
}
