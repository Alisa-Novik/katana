package com.norwood.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import com.norwood.networking.KatanaServer;
import com.norwood.routing.Route;
import com.norwood.routing.Router;
import com.norwood.routing.annotation.Get;

public class KatanaCore {
    private static final Container container = new KatanaContainer();
    public static final Set<Class<?>> beanRegistryDefinitions = new HashSet<>();
    private Router router;

    public void boot() {
        System.out.println("Booting Katana");
        (new Thread(() -> KatanaServer.withCore(this))).start();
        System.out.println("Katana booted. Awaiting connections form clients...");

        try {
            registerMainBeans();
        } catch (ContainerException e) {
            throw new RuntimeException("Error initializing container exception.");
        }

        processAutowiring();
    }

    private void registerMainBeans() throws ContainerException {
        Container container = getContainer();

        router = new Router();
        container.set(Router.class, router);
    }

    private void processAutowiring() {
        System.out.println("Processing routes...");

        for (Class<?> userClass : beanRegistryDefinitions) {

            System.out.println("Processing class: " + userClass);
            for (Method method : userClass.getMethods()) {
                for (Annotation an : method.getAnnotations()) {
                    if (an instanceof Get a) {
                        String path = a.path();
                        if (router.hasRouteWithPath(path)) {
                            throw new RuntimeException("Route already define with path: " + path);
                        }


                        router.defineRoutes(List.of(
                            Route.get(path, createHandler(method))
                        ));
                    }
                }
            }
        }
    }

    private BiConsumer<Object, HttpRequest> createHandler(Method method) {
        return (instance, request) -> invokeMethod(method, instance, request);
    }

    private void invokeMethod(Method method, Object instance, Object arg1) {
        try {
            method.invoke(instance, arg1);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error invoking stuff...");
            e.printStackTrace();
        }
    }

    public static Container getContainer() {
        return container;
    }

    public void handleRequest(HttpRequest req) {
        System.out.println("Processing user request..." + req.uri().getRawPath());
        router.route(req);
    }
}
