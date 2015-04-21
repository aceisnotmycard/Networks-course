package bb8uploader;

import bb8messaging.Actioner;
import bb8messaging.Dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by aceisnotmycard on 4/21/15.
 */
public class FileSender implements Actioner {

    private Dialog dialog;
    private File file;

    public FileSender(Socket socket) {
        dialog = new Dialog(socket);
    }

    //TODO: verify file
    public boolean setFile(String path) {
        file = new File(path);
        return true;
    }

    // Sends file:
    // file name
    // file size
    // file itself
    @Override
    public boolean exec() {
        System.out.println("Sending file");
        if (file == null) {
            System.out.println("File is not specified");
            return false;
        }
        dialog.sendMessage(file.getName());
        Long fileLength = file.length();
        dialog.sendMessage(fileLength.toString());
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        dialog.sendFile(fileInputStream);
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.sendCommit();
        boolean success = new Boolean(dialog.receiveMessage());
        return success;
    }
}
