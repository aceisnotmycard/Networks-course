package bb8uploader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aceisnotmycard on 4/21/15.
 */
public class Server {
    ServerSocket socket;

    public Server(int port) {
        try {
            socket = new ServerSocket(port);
            while (true) {
                Socket accepted = socket.accept();
                FileKeeper keeper = new FileKeeper(accepted, "upload/");
                if (keeper.exec()) {
                    System.out.println("Successfully received file");
                } else {
                    System.out.println("Something went wrong");
                }
                accepted.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
