package com.norwood.routing;

import java.net.http.HttpRequest;
import java.util.Locale;
import java.util.function.BiFunction;

public class Route {
    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH;

        public static HttpMethod fromString(String method) {
            if (method == null) {
                throw new IllegalArgumentException("HTTP method cannot be null");
            }
            return HttpMethod.valueOf(method.toUpperCase(Locale.ROOT));
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    private final HttpMethod method;
    private final String path;
    private final Object handler;

    private Route(HttpMethod method, String name, Object handler) {
        this.method = method;
        this.path = name;
        this.handler = handler;
    }

    private static Route create(HttpMethod method, String path, Object handler) {
        return new Route(method, path, handler);
    }

    public boolean ofPath(String path) {
        return this.path.equals(path);
    }

    public static Route get(String name, BiFunction<Object, HttpRequest, Object> handler) {
        return Route.create(HttpMethod.GET, name, handler);
    }

    public static Route post(String name, BiFunction<Object, HttpRequest, Object> handler) {
        return Route.create(HttpMethod.POST, name, handler);
    }

    @Override
    public String toString() {
        return "Route name '" + method.toString() + " " + path + "'";
    }

    @SuppressWarnings("unchecked")
    public BiFunction<Object, HttpRequest, Object> handler() {
        return (BiFunction<Object, HttpRequest, Object>) handler;
    }

    public String path() {
        return path;
    }

}
