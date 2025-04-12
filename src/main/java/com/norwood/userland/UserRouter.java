package com.norwood.userland;

import java.net.http.HttpRequest;
import java.util.List;

import com.norwood.core.KatanaCore;
import com.norwood.core.Singleton;
import com.norwood.routing.Route;
import com.norwood.routing.Router;

@Singleton
public class UserRouter {
    public void defineRoutes() {
        getRouter().defineRoutes(
            List.of(
                Route.get("/test1", this::handler1),
                Route.get("/test2", this::handler2)
            )
        );
    }

    private Router getRouter() {
        return KatanaCore.getContainer().get(Router.class);
    }

    void handler1(HttpRequest request) {
        System.out.println("/test1 executed :" + request.method());
    }

    void handler2(HttpRequest request) {
        System.out.println("/test2 executed :" + request.method());
    }

    void handler3(HttpRequest request) {
        System.out.println("/test3 executed :" + request.method());
    }
}
