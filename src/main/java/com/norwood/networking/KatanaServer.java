package com.norwood.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.norwood.core.KatanaCore;
import com.norwood.core.KatanaResponse;
import com.norwood.util.HttpRequestSerializer;

public class KatanaServer {
    public static final int SERVER_PORT = 8082;
    private static boolean running = true;
    private ServerSocket socket;
    private KatanaCore core;

    private KatanaServer(KatanaCore core) {
        this.core = core;
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
            BufferedReader reader = createSocketReader(clientSocket);
            PrintWriter writer = createSocketWriter(clientSocket);
        ) {
            String requestLine = reader.readLine();

            String header;
            while ((header = reader.readLine()) != null && !header.isEmpty()) {
                System.out.println("(ignoring) Got request header: " + header);
            }

            if (requestLine != null) {
                KatanaResponse response = core.handleRequest(
                    HttpRequestSerializer.unserialize(requestLine)
                );
                writeHttpOk(writer);
                writer.print(response.value());
                writer.flush();

            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // nothing
        }
        System.out.println("Handled client:" + clientSocket.getInetAddress().toString());
    }

    private void writeHttpOk(PrintWriter writer) {
        writer.print("HTTP/1.1 200 OK\r\n");
        writer.print("Content-Type: text/html; charset=utf-8\r\n");
        writer.print("Connection: close\r\n");
        writer.print("\r\n");
    }

    private PrintWriter createSocketWriter(Socket clientSocket) throws IOException {
        return new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private BufferedReader createSocketReader(Socket clientSocket) throws IOException {
        return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static KatanaServer withCore(KatanaCore core) {
        return new KatanaServer(core);
    }
}
