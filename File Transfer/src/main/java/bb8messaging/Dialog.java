package bb8messaging;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UnknownFormatConversionException;

/**
 * Simple messaging protocol
 */
public class Dialog {
    public static final String HEADER = "BB8D";
    public static final int VERSION = 1;
    public static final int WRONG_ACTION_CODE = 0;

    private static final int CHUNK_SIZE = 65536;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Dialog(Socket socket) {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // Sending information about client's version of protocol
    public void sendHeader() {
        try {
            outputStream.write(getUTF8Bytes(HEADER));
            outputStream.writeInt(VERSION);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // Receiving information about another talker
    // returns true if received information is correct
    public boolean receiveAndCompareHeader() {
        try {
            byte[] data = new byte[4];
            for (int i = 0; i < 4; i++) {
                data[i] = inputStream.readByte();
            }
            String receivedHeader = new String(data, StandardCharsets.UTF_8.name());
            if (!receivedHeader.equals(HEADER)) {
                sendMessage("Please use " + HEADER + " protocol");
                return false;
            }
            int receivedVersion = inputStream.readInt();
            if (receivedVersion != VERSION) {
                sendMessage("Sorry i'm using v. " + ((Integer) VERSION).toString() + " protocol");
                return false;
            }
            sendMessage("Connection established");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return true;
    }

    public void sendMessage(String message) {
        try {
            byte[] data = getUTF8Bytes(message);
            outputStream.writeInt(data.length);
            outputStream.write(data);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
     }

    // Receive message in UTF-8 encoding
    public String receiveMessage() {
        try {
            int length = inputStream.readInt();
            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                data[i] = inputStream.readByte();
            }
            return new String(data, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // Send integer representation of method
    public void sendActionCode(int actionCode) {
        try {
            outputStream.writeInt(actionCode);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // returns WRONG_ACTION_CODE if something went wrong
    public int receiveActionCode() {
        int receivedCode = WRONG_ACTION_CODE;
        try {
            receivedCode = inputStream.readInt();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return receivedCode;
    }

    //
    public void sendFile(FileInputStream fileInputStream) {
        try {
            byte[] fileChunk = new byte[CHUNK_SIZE];
            int count = 0;
            while ((count = fileInputStream.read(fileChunk)) != -1) {
                outputStream.write(fileChunk, 0, count);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("File sent.");
    }

    //Need to abstract from filename and location
    public void receiveFile(FileOutputStream fileOutputStream, long fileSize) {
        try {
            byte[] receivedChunk = new byte[CHUNK_SIZE];
            int count = 0;
            long sum = 0;
            while ((count = inputStream.read(receivedChunk)) != -1) {
                fileOutputStream.write(receivedChunk, 0, count);
                sum += count;
                if (sum == fileSize) break;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("File received.");
    }

    public void sendCommit() {
        try {
            outputStream.writeBoolean(true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean receiveCommit() {
        try {
            return inputStream.readBoolean();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: test StandardCharsets.UTF_8.name()
    private byte[] getUTF8Bytes(String string) {
        try {
            return string.getBytes(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
