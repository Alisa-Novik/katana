package com.norwood.userland;

import java.net.http.HttpRequest;

import com.norwood.core.Singleton;
import com.norwood.routing.annotation.Get;

@Singleton
public class UserRouter {

    @Get(path = "/test1")
    public void route1(HttpRequest request) {
        System.out.println("/test1 executed :" + request.method());
    }

    @Get(path = "/test1")
    public void route2(HttpRequest request) {
        System.out.println("/test2 executed :" + request.method());
    }
}
