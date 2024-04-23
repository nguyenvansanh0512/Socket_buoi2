package Bai_1;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiClientServer {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                Runnable clientTask = new ClientHandler(clientSocket);
                executor.execute(clientTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                for (int i = 1; i <= 1000; i++) {
                    out.println(i);
                    Thread.sleep(1000); // Đợi 1 giây trước khi gửi số tiếp theo
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}
