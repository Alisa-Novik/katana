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

    public void sendRequest(HttpRequest request) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            // out.println(HttpRequestSerializer.serialize(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpRequest createRequest(String path) {
        return HttpRequest.newBuilder()
            .uri(URI.create("https://localhost:8082" + path))
            .GET()
            .build();
    }
}
