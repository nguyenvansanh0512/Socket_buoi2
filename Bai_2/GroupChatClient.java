package Bai_2;
import java.io.*;
import java.net.*;

public class GroupChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static String username;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println(in.readLine()); // Nhận yêu cầu nhập username từ server
            username = stdIn.readLine(); // Nhập username từ bàn phím
            out.println(username); // Gửi username tới server

            new Thread(new ServerListener(in)).start(); // Bắt đầu lắng nghe tin nhắn từ server

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput); // Gửi tin nhắn từ client tới server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerListener implements Runnable {
        private BufferedReader in;

        public ServerListener(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println(serverResponse); // Hiển thị tin nhắn từ server
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
