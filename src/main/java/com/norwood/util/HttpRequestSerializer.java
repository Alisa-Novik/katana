package com.norwood.util;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestSerializer
{
    public static String serialize(HttpRequest req) {
        String topString = String.format("%s %s HTTP/1.1", req.method(), req.uri().getPath());

        return topString;
    }
    
    public static String getPath(URI uri) {
        return uri.getRawPath() + 
            (uri.getRawQuery() != null ? "?" + uri.getRawQuery() : "") +
            (uri.getRawFragment() != null ? "#" + uri.getRawFragment() : "");
    }
}
