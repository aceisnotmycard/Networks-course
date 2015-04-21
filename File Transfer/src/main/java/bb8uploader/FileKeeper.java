package bb8uploader;

import bb8messaging.Actioner;
import bb8messaging.Dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by aceisnotmycard on 4/21/15.
 */
public class FileKeeper implements Actioner {

    private String storeLocation;
    private Dialog dialog;

    FileKeeper(Socket socket) {
        dialog = new Dialog(socket);
    }

    FileKeeper(Socket socket, String storeLocation) {
        this.storeLocation = storeLocation;
        dialog = new Dialog(socket);
    }

    // Data order:
    // file name
    // file size
    // file itself
    @Override
    public boolean exec() {
        System.out.println("Receiving file...");
        String filename = dialog.receiveMessage();
        System.out.println("Filename: " + filename);
        Long fileSize = new Long(dialog.receiveMessage());
        System.out.println("Received file size:" + fileSize);
        File directory = new File(storeLocation);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(storeLocation + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        dialog.receiveFile(fileOutputStream, fileSize);
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!dialog.receiveCommit()) {
            System.out.println("Commit is not received");
            return false;
        }
        File file = new File(storeLocation + filename);
        System.out.println("Actual received file size:" + file.length());

        if (file.length() != fileSize) {
            if (file.delete()) {
                dialog.sendMessage(Boolean.FALSE.toString());
            }
            return false;
        } else {
            dialog.sendMessage(Boolean.TRUE.toString());
        }
        return true;
    }
}
