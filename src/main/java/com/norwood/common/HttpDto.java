package com.norwood.common;

import java.net.http.HttpRequest;

public record HttpDto(
    String method,
    String uri
    // Map<String, List<String>> headers,
    // String body
) {
    public static HttpDto from(HttpRequest httpRequest) {
        return new HttpDto(
            httpRequest.method(),
            httpRequest.uri().toString()
        );
    }

    @Override
    public final String toString() {
        return String.format("%s %s HTTP/1.1", method, uri);
    }
}
