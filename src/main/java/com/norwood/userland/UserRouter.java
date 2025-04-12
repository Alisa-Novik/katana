package com.norwood.userland;

import java.net.http.HttpRequest;

import com.norwood.core.Singleton;
import com.norwood.routing.annotation.Get;

@Singleton
public class UserRouter {

    @Get(path = "/test1")
    public void route1(HttpRequest request) {
        System.out.println("[UserRouter] /test1 executed :" + request.method());
    }

    @Get(path = "/test2")
    public void route2(HttpRequest request) {
        System.out.println("[UserRouter] /test2 executed :" + request.method());
    }
}
