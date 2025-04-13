package com.norwood.routing;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.norwood.core.BeanRegistry;
import com.norwood.userland.UserController;

public class Router {
    final List<Route> routes = new ArrayList<>();

    public Object route(HttpRequest request) {
        return findRouteByPath(request).handler().apply(resolveController(), request);
    }

    private UserController resolveController() {
        return registry().get(UserController.class);
    }

    private Route findRouteByPath(HttpRequest request) {
        String path = request.uri().getRawPath();
        return routes.stream()
                .filter(r -> r.ofPath(path))
                .findFirst().orElseThrow();
    }

    private BeanRegistry registry() {
        return BeanRegistry.instance();
    }

    public void defineRoutes(List<Route> other) {
        System.out.println("adding route: " + other.get(0).path());
        routes.addAll(other);
    }

    public boolean hasRouteWithPath(String path) {
        return routes.stream().anyMatch(r -> r.ofPath(path));
    }
}
