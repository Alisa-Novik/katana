package com.norwood.routing;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.norwood.core.KatanaBean;
import com.norwood.core.KatanaCore;
import com.norwood.userland.UserRouter;

public class Router implements KatanaBean
{
    final List<Route> routes = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public void route(HttpRequest request) {
        String path = request.uri().getRawPath();
        Optional<Route> routeOpt = routes.stream()
            .filter(r -> r.ofPath(path))
            .findFirst();

        if (routeOpt.isEmpty()) {
            throw new RuntimeException("Route not found");
        }

        Route route = routeOpt.get();

        BiConsumer<Object, HttpRequest> handler = (BiConsumer<Object, HttpRequest>) route.handler();

        UserRouter router = (UserRouter) KatanaCore.beanRegistry.get(UserRouter.class);

        handler.accept(router, request);
    }

    public void defineDefaultRoutes() {
        // routes.addAll(List.of(
        //     Route.get("/hello", s -> System.out.println("Henlo, " + s))
        // ));
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
