package com.norwood.routing;

import com.norwood.core.KatanaCore;
import com.norwood.userland.UserController;
import junit.framework.TestCase;

import java.net.URI;
import java.net.http.HttpRequest;

public class RouterTest extends TestCase {
    public void testDefineAndHasRoute() {
        Router r = new Router();
        r.defineRoute(Route.get("/a", (o, req) -> "ok"));
        assertTrue(r.hasRouteWithPath("/a"));
        assertFalse(r.hasRouteWithPath("/b"));
    }

    public void testRouteCallsHandler() {
        Router r = new Router();
        if (KatanaCore.container.get(UserController.class) == null) {
            KatanaCore.container.set(UserController.class, new UserController());
        }
        r.defineRoute(Route.get("/c", (o, req) -> "res"));
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://x/c")).GET().build();
        assertEquals("res", r.route(req));
    }
}
