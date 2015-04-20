package bb8uploader;

import bb8messaging.Dialog;

import java.net.Socket;
import java.util.HashMap;


/**
 * Just made-on-kolenka wrapper for bb8 library
 *
 * "Hello" – establish connection to server
 * "Upload" – upload new file to server
 * "Bye" – close connection
 */
public class ActionFacade {
    Dialog dialog;

    public enum ActionCode {
        HELLO(1),
        UPLOAD(2),
        BYE(3);

        private int index;

        ActionCode(int index) {
            this.index = index;
        }

        public int getValue() {
            return index;
        }
    }

     ActionFacade(Socket socket) {
        dialog = new Dialog(socket);
    }


}
