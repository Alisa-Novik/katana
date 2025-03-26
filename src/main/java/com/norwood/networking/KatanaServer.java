package com.norwood.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpRequest;

import com.norwood.routing.Router;
import com.norwood.userland.UserRouter;
import com.norwood.util.KatanaClient;

public class KatanaServer
{
    public static final int SERVER_PORT = 8082;
    private static KatanaServer instance;
    private static boolean running = true;
    private ServerSocket socket;
    private Router router = new Router();

    private KatanaServer () {
        try {
            this.socket = new ServerSocket(SERVER_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bootServer();
    }

    public void bootServer() {
        System.out.println("Started server. Listening to " + SERVER_PORT);
        while (running) {
            try {
                Socket clientSocket = socket.accept(); 
                new Thread(() -> handleClient(clientSocket)).start();
            } catch (Exception e) {
                 System.out.println("Error during client handling: " + e.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String message;
            while ((message = reader.readLine()) != null) {
                HttpRequest req = KatanaClient.request();
                (new UserRouter()).defineRoutes(req);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
        }
        System.out.println("Handled client:" + clientSocket.getInetAddress().toString());
    }

    public static KatanaServer getInstance() {
        if (instance == null) {
            instance = new KatanaServer();
        }
        return instance;
    }

    public void acceptRequest(HttpRequest request) {
        
    }
}
