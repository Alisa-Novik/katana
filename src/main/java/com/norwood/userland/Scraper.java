package com.norwood.userland;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class Scraper {
    public void scrape() {
        System.out.println("test");
    }

    public void sendRequest(String uriStr, String method) {
        try {
            HttpClient.newHttpClient().send(
                createRequest(uriStr, method),
                BodyHandlers.discarding()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HttpRequest createRequest(String uriStr, String method) {
        return HttpRequest.newBuilder()
            .uri(URI.create(uriStr))
            .method(method, BodyPublishers.noBody())
            .build();
    }
}

