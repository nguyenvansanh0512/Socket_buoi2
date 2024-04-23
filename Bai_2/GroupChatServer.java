package Bai_2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GroupChatServer {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 10;
    private static final ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

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
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println("Enter your username:");
                username = in.readLine();
                System.out.println("User " + username + " joined.");

                clients.put(username, out);
                broadcast(username + " joined the chat.");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    broadcast(username + ": " + inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clients.remove(username);
                    broadcast(username + " left the chat.");
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clients.values()) {
                writer.println(message);
            }
        }
    }
}

