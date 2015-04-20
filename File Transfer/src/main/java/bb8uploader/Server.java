package bb8uploader;

import java.io.File;
import java.net.Socket;


public class Server {
    ActionFacade facade;

    private static final String UPLOAD_DIRECTORY = "upload/";

    public Server(Socket socket) {
        facade = new ActionFacade(socket);
    }

    public void listen() {
        if (facade.dialog.receiveAndCompareHeader()) {
            System.out.println("Client connected");
            while (true) {
                if (facade.dialog.receiveActionCode() == ActionFacade.ActionCode.BYE.getValue()) {
                    System.out.println("Client disconnected");
                    return;
                }
                if (facade.dialog.receiveActionCode() == ActionFacade.ActionCode.UPLOAD.getValue()) {
                    System.err.println("Client is trying to upload file");
                    String filename = facade.dialog.receiveMessage();
                    File file = new File(UPLOAD_DIRECTORY + filename);
                    System.out.println("Uploading " + filename + "...");
                    facade.dialog.receiveFile(file);
                    if (facade.dialog.receiveCommit()) {
                        facade.dialog.sendMessage("Upload successful");
                        System.out.println("Done.");
                    }
                }
            }
        }
    }
}
