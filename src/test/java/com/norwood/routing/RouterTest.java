package com.norwood.routing;

import java.net.http.HttpRequest;

import com.norwood.core.KatanaCore;
import com.norwood.userland.UserController;
import com.norwood.util.HttpRequestSerializer;

import junit.framework.TestCase;

public class RouterTest extends TestCase {
    private Router router;

    @Override
    protected void setUp() throws Exception {
        router = new Router();
        // register a controller instance if not already present
        if (KatanaCore.container.get(UserController.class) == null) {
            KatanaCore.container.set(UserController.class, new UserController());
        }
    }

    public void testDefineRouteAndRoute() {
        router.defineRoute(Route.get("/a", (c, r) -> "A"));
        router.defineRoute(Route.post("/b", (c, r) -> "B"));
        router.defineRoute(Route.put("/c", (c, r) -> "C"));

        HttpRequest reqA = HttpRequestSerializer.unserialize("GET /a HTTP/1.1");
        assertEquals("A", router.route(reqA));

        HttpRequest reqB = HttpRequestSerializer.unserialize("POST /b HTTP/1.1");
        assertEquals("B", router.route(reqB));

        HttpRequest reqC = HttpRequestSerializer.unserialize("PUT /c HTTP/1.1");
        assertEquals("C", router.route(reqC));
    }

    public void testHasRouteDetectsDuplicateDefinitions() {
        router.defineRoute(Route.get("/dup", (c, r) -> "first"));
        assertTrue(router.hasRoute("/dup", Route.HttpMethod.GET));

        router.defineRoute(Route.get("/dup", (c, r) -> "second"));
        assertTrue(router.hasRoute("/dup", Route.HttpMethod.GET));
    }

    public void testHasRouteWithPathAcrossMethods() {
        router.defineRoute(Route.get("/multi", (c, r) -> "GET"));
        router.defineRoute(Route.post("/multi", (c, r) -> "POST"));

        assertTrue(router.hasRoute("/multi", Route.HttpMethod.GET));
        assertTrue(router.hasRoute("/multi", Route.HttpMethod.POST));
        assertTrue(router.hasRouteWithPath("/multi"));
        assertFalse(router.hasRouteWithPath("/missing"));
    }
}
