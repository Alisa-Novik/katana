package com.norwood.routing;

import java.util.ArrayList;
import java.util.List;

import com.norwood.core.KatanaBean;

public class Router implements KatanaBean
{
    final List<Route> routes = new ArrayList<>();

    public void route(String message) {
        String[] httpRequest = message.split(" ");
        String method = httpRequest[0];
        String path = httpRequest[1];

        System.out.println("Method: " + method + " Path: " + path);
    }

    public void defineDefaultRoutes() {
        routes.addAll(List.of(
            Route.get("/hello", s -> System.out.println("Henlo, " + s))
        ));
    }

    public void defineRoutes(List<Route> other) {
        routes.addAll(other);
    }

    public boolean hasRouteWithPath(String path) {
        return routes.stream().anyMatch(
            r -> r.path().equals(path)
        );
    }
}
