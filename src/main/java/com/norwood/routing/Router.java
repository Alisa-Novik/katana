package com.norwood.routing;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.norwood.core.BeanRegistry;
import com.norwood.core.KatanaBean;
import com.norwood.userland.UserRouter;

public class Router implements KatanaBean {
    final List<Route> routes = new ArrayList<>();

    public void route(HttpRequest request) {
        findByPath(request).handler().accept(
                registry().get(UserRouter.class),
                request
        );
    }

    private Route findByPath(HttpRequest request) {
        String path = request.uri().getRawPath();
        return routes.stream()
                .filter(r -> r.ofPath(path))
                .findFirst().orElseThrow();
    }

    private BeanRegistry registry() {
        return BeanRegistry.instance();
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
