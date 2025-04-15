package com.norwood.userland;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import com.norwood.core.Singleton;
import com.norwood.core.annotations.Inject;
import com.norwood.routing.annotation.Get;
import com.norwood.routing.annotation.Post;

@Singleton
public class UserController {
    @Inject UserService userService;

    @Post(path = "/test1")
    public int route1(HttpRequest request) {
        System.out.println("[UserController] /test1 executed :" + request.method());
        return 1;
    }

    @Get(path = "/test3")
    public String userServiceTest(HttpRequest request) {
        return userService.userMethod();
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
