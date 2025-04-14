package com.norwood.userland;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;

import com.norwood.core.Singleton;
import com.norwood.routing.annotation.Get;

@Singleton
public class UserController {
    @Get(path = "/test1")
    public int route1(HttpRequest request) {
        System.out.println("[UserController] /test1 executed :" + request.method());
        return 1;
    }

    @Get(path = "/test2")
    public String route2(HttpRequest request) {
        System.out.println("[UserController] /test2 executed :" + request.method());
        try {
            return Files.readString(Path.of("resources/index.html"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to return index.html");
        }
    }
}
