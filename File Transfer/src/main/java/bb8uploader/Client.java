package bb8uploader;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by aceisnotmycard on 4/21/15.
 */
public class Client {
    Socket socket;

    public Client(String host, int port, String path) {
        try {
            socket = new Socket(host, port);
            FileSender sender = new FileSender(socket);
            sender.setFile(path);
            if (sender.exec()) {
                System.out.println("File successfully send");
            } else {
                System.out.println("Something went wrong");
            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Cannot connect to the server: " + e.getMessage());
        }
    }

}
