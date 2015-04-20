package bb8uploader;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Made on kolenka
 */
public class Client {
    HashMap<String, Integer> stringToActionCode;
    ActionFacade facade;
    boolean connectionEstablished;

    String[] commands = new String[] {
        "hello",
        "upload",
        "bye"
    };

    List<String> commandsList = new ArrayList<String>(Arrays.asList(commands));

    public Client(Socket socket) {
        facade = new ActionFacade(socket);
        connectionEstablished = false;
        stringToActionCode = new HashMap<String, Integer>();
        stringToActionCode.put(commandsList.get(0), ActionFacade.ActionCode.HELLO.getValue());
        stringToActionCode.put(commandsList.get(1), ActionFacade.ActionCode.UPLOAD.getValue());
        stringToActionCode.put(commandsList.get(2), ActionFacade.ActionCode.UPLOAD.getValue());
    }

    // Ugly, yeah
    public boolean parseInput(String string) {
        String[] parts = string.toLowerCase().split("\\s+");
        if(commandsList.contains(parts[0])) {
            if (!connectionEstablished) {
                establishConnection(parts[0]);
            } else {
                if (stringToActionCode.get(parts[0]) == ActionFacade.ActionCode.BYE.getValue()) {
                    facade.dialog.sendActionCode(ActionFacade.ActionCode.BYE.getValue());
                    return true;
                } else if (stringToActionCode.get(parts[0]) == ActionFacade.ActionCode.HELLO.getValue()) {
                    System.out.println("You're already connected!");
                } else if (stringToActionCode.get(parts[0]) == ActionFacade.ActionCode.UPLOAD.getValue()) {
                    uploadFile(parts[1]);
                }
            }
        } else {
            System.out.println("I don't know what you mean :(");
            return false;
        }
        return false;
    }

    private void establishConnection(String arg) {
        if (stringToActionCode.get(arg) != ActionFacade.ActionCode.HELLO.getValue()) {
            System.out.println("Hey! You should greet server. Say 'hello'!");
        } else {
            facade.dialog.sendHeader();
            System.out.println(facade.dialog.receiveMessage());
            connectionEstablished = true;
        }
    }

    private void uploadFile(String path) {
        if (path == null) {
            System.out.println("Please add path to file");
        } else {
            facade.dialog.sendActionCode(ActionFacade.ActionCode.UPLOAD.getValue());
            File file = new File(path);
            facade.dialog.sendMessage(file.getName());
            facade.dialog.sendFile(file);
            facade.dialog.sendCommit();
            System.out.println("File sent");
            System.out.println(facade.dialog.receiveMessage());
        }
    }
}
