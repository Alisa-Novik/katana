package com.norwood.routing;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.norwood.core.KatanaCore;
import com.norwood.userland.UserController;

public class Router {
    final List<Route> routes = new ArrayList<>();

    public Object route(HttpRequest request) {
        System.out.println(resolveController());
        return findRoute(request.method(), request.uri().getRawPath())
            .handler().apply(resolveController(), request);
    }

    private UserController resolveController() {
        return KatanaCore.container.get(UserController.class);
    }

    private Route findRoute(String method, String path) {
        return routes.stream()
                .filter(r -> r.matches(method, path))
                .findFirst().orElseThrow();
    }

    public void defineRoute(Route route) {
        routes.add(route);
    }

    public void defineRoutes(List<Route> other) {
        routes.addAll(other);
    }

    public boolean hasRoute(String path, Route.HttpMethod method) {
        return routes.stream().anyMatch(r -> r.ofPath(path) && r.method() == method);
    }

    public boolean hasRouteWithPath(String path) {
        return routes.stream().anyMatch(r -> r.ofPath(path));
    }
}
