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
        return findRouteByPath(request).handler().apply(resolveController(), request);
    }

    private UserController resolveController() {
        return KatanaCore.container.get(UserController.class);
    }

    private Route findRouteByPath(HttpRequest request) {
        String path = request.uri().getRawPath();
        return routes.stream()
                .filter(r -> r.ofPath(path))
                .findFirst().orElseThrow();
    }

    public void defineRoute(Route route) {
        routes.add(route);
    }

    public void defineRoutes(List<Route> other) {
        routes.addAll(other);
    }

    public boolean hasRouteWithPath(String path) {
        return routes.stream().anyMatch(r -> r.ofPath(path));
    }
}
