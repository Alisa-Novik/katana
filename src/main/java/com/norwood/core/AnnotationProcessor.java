package com.norwood.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.function.BiFunction;

import com.norwood.routing.Route;
import com.norwood.routing.Router;
import com.norwood.routing.annotation.Get;

public class AnnotationProcessor {
    public void processAnnotations(List<Class<?>> classDefinitions, Router router) {
        for (Class<?> userClass : classDefinitions) {
            System.out.println("Processing class: " + userClass);
            for (Method method : userClass.getMethods()) {
                for (Annotation an : method.getAnnotations()) {
                    switch (an) {
                        case Get a -> route(a, router, method);
                        default -> noop();
                    }
                }
            }
        }
    }

    private void noop() {
        // nothing
    }

    private void route(Get a, Router router, Method method) {
        String path = a.path();
        if (router.hasRouteWithPath(path)) {
            throw new RuntimeException("Route already define with path: " + path);
        }

        router.defineRoutes(List.of(
            Route.get(path, createHandler(method))
        ));
    }

    private BiFunction<Object, HttpRequest, Object> createHandler(Method method) {
        return (instance, request) -> invokeMethod(method, instance, request);
    }

    private Object invokeMethod(Method method, Object instance, Object arg1) {
        try {
            return method.invoke(instance, arg1);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error invoking stuff...");
            e.printStackTrace();
            throw new RuntimeException("Failed executing method: " + method.getName());
         }
    }

}

