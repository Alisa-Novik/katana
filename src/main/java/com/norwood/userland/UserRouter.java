package com.norwood.userland;

import java.net.http.HttpRequest;
import java.util.function.Consumer;

import com.norwood.routing.Route;
import com.norwood.routing.Router;

public class UserRouter {
    public void defineRoutes() {
        Router.defineRoutes(
            Route.get("/test1", this::handler1),
            Route.get("/test2", this::handler2)
        );
    }

    void handler1(HttpRequest request) {
        System.out.println("/test1 executed :" + request.method());
    }

    void handler2(HttpRequest request) {
        System.out.println("/test2 executed :" + request.method());
    }
}
