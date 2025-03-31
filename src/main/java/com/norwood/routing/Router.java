package com.norwood.routing;

public class Router
{
    public void route(String message) {
        String[] httpRequest = message.split(" ");
        String method = httpRequest[0];
        String path = httpRequest[1];

        System.out.println("Method: " + method + " Path: " + path);
    }

    public static void defineRoutes(Route... routes) {
        for (Route route : routes) {

        }
    }
}
