package com.norwood;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpRequest;

import junit.framework.TestCase;

import com.norwood.core.KatanaCore;
import com.norwood.routing.Route;
import com.norwood.routing.Router;
import com.norwood.userland.UserController;

public class RouterTest extends TestCase {
    public void testPathParameterRouting() throws Exception {
        Router router = new Router();
        // register controller in container
        try {
            KatanaCore.container.set(UserController.class, new UserController());
        } catch (Exception ignored) {}
        Method m = UserController.class.getMethod("routeUser", String.class, HttpRequest.class);
        router.defineRoute(Route.get("/users/{id}", m));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("http://localhost/users/123"))
                .GET()
                .build();

        Object result = router.route(req);
        assertEquals("user:123", result);
    }
}
