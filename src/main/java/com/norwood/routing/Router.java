package com.norwood.routing;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.norwood.core.KatanaBean;

public class Router implements KatanaBean
{
    final List<Route> routes = new ArrayList<>();

    public void route(HttpRequest request) {
        routes.stream().filter(
            (r -> request.uri().getRawPath().equals(r.path()))
        ).;
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
