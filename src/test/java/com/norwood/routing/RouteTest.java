package com.norwood.routing;

import junit.framework.TestCase;

public class RouteTest extends TestCase {
    public void testHttpMethodFromString() {
        assertEquals(Route.HttpMethod.GET, Route.HttpMethod.fromString("get"));
        assertEquals(Route.HttpMethod.POST, Route.HttpMethod.fromString("POST"));
    }

    public void testOfPathAndToString() {
        Route r = Route.get("/x", (o, req) -> "ok");
        assertTrue(r.ofPath("/x"));
        assertTrue(r.toString().contains("GET /x"));
    }

    public void testOfPathFalse() {
        Route r = Route.post("/y", (o, req) -> "a");
        assertFalse(r.ofPath("/x"));
    }
}
