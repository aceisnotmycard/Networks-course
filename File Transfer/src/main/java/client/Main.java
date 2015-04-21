package client;

import bb8uploader.Client;

/**
 * Created by aceisnotmycard on 4/21/15.
 */
public class Main {
    public static void main(String[] args) {
        String host = args[0];
        Integer port = new Integer(args[1]);
        String path = args[2];
        Client client = new Client(host, port, path);
    }
}
