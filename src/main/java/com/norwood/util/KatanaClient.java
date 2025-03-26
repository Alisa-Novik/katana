package com.norwood.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpRequest;

import com.norwood.networking.KatanaServer;

public class KatanaClient
{
    private PrintWriter out;
    private Socket socket;

    public KatanaClient() {
        try {
            this.socket = new Socket("127.0.0.1", KatanaServer.SERVER_PORT);
            socket.setKeepAlive(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRequest() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(HttpRequestSerializer.serialize(createRequest()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HttpRequest request() {
        return HttpRequest.newBuilder()
            .uri(URI.create("https://localhost:8082/test/get"))
            .GET()
            .build();
    }

    public HttpRequest createRequest() {
        return HttpRequest.newBuilder()
            .uri(URI.create("https://localhost:8082/test/get"))
            .GET()
            .build();
    }
    //
    // public void run() {
    //     new Thread(() -> {
    //         try (
    //             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    //         ) {
    //             this.out = out;
    //
    //             while (running) {
    //                 handleCommand(commands.take());
    //             }
    //         } catch (Exception e) {
    //             System.err.println("Sending Error: " + e.getMessage());
    //             System.exit(1);
    //         } 
    //     }).start();
    // }
}
