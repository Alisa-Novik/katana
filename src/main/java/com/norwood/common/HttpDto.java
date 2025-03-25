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
}
