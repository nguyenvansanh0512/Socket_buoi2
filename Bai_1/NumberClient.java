package Bai_1;
import java.io.*;
import java.net.*;

public class NumberClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String number;
            while ((number = in.readLine()) != null) {
                System.out.println("Received number: " + number);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

