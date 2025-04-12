package com.norwood.util;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestSerializer
{
    public static HttpRequest unserialize(String req) {
        String[] httpRequest = req.split(" ");

        // TODO: multiple methods support
        String method = httpRequest[0];
        String path = httpRequest[1];

        System.out.println("Method: " + method + " Path: " + path);

        String host = "https://localhost:8082";
        return HttpRequest.newBuilder()
            .uri(URI.create(host + path))
            .GET()
            .build();
    }

    public static String serialize(HttpRequest req) {
        return String.format("%s %s HTTP/1.1", req.method(), req.uri().getPath());
    }
    
    public static String getPath(URI uri) {
        return uri.getRawPath() + 
            (uri.getRawQuery() != null ? "?" + uri.getRawQuery() : "") +
            (uri.getRawFragment() != null ? "#" + uri.getRawFragment() : "");
    }
}
