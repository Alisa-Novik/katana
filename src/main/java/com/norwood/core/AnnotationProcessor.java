package com.norwood.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.function.BiFunction;

import com.norwood.core.annotations.Inject;
import com.norwood.routing.Route;
import com.norwood.routing.Router;
import com.norwood.routing.annotation.Get;
import com.norwood.routing.annotation.Post;
import com.norwood.routing.annotation.Put;
import com.norwood.routing.annotation.Delete;
import com.norwood.routing.annotation.Patch;
import com.norwood.routing.annotation.Head;
import com.norwood.routing.annotation.Options;

public class AnnotationProcessor {
    public void processAnnotations(List<Class<?>> classDefinitions, Router router) {
        for (Class<?> userClass : classDefinitions) {
            for (Field field : userClass.getDeclaredFields()) {
                for (Annotation an : field.getDeclaredAnnotations()) {
                    System.out.println("Anno: " + an.getClass());
                    switch (an) {
                        case Inject _ -> inject(field);
                        default -> System.out.println("Unknown annotation: " + an.toString());
                    }
                }
            }
            for (Method method : userClass.getMethods()) {
                for (Annotation an : method.getAnnotations()) {
                    switch (an) {
                        case Get a -> routeGet(a, router, method);
                        case Post a -> routePost(a, router, method);
                        case Put a -> routePut(a, router, method);
                        case Delete a -> routeDelete(a, router, method);
                        case Patch a -> routePatch(a, router, method);
                        case Head a -> routeHead(a, router, method);
                        case Options a -> routeOptions(a, router, method);
                        default -> System.out.println("Unknown annotation: " + an.toString());
                    }
                }
            }
        }
    }

    private void inject(Field field) {
        try {
            Class<?> fieldType = field.getType();
            Object owner = container().get(field.getDeclaringClass());
            Object dependency = fieldType.getDeclaredConstructor().newInstance();

            field.setAccessible(true);
            field.set(owner, dependency);

            System.out.println(owner);
            System.out.println(dependency);
        } catch (InstantiationException  | 
                IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to load user-defined beans.");
        }
    }

    private void routePost(Post a, Router router, Method method) {
        String path = a.path();
        if (router.hasRoute(path, Route.HttpMethod.POST)) {
            throw new RuntimeException("Route already defined with path: " + path);
        }

        router.defineRoute(Route.post(path, createHandler(method)));
    }

    private void routeGet(Get a, Router router, Method method) {
        String path = a.path();
        if (router.hasRoute(path, Route.HttpMethod.GET)) {
            throw new RuntimeException("Route already defined with path: " + path);
        }

        router.defineRoute(Route.get(path, createHandler(method)));
    }

    private void routePut(Put a, Router router, Method method) {
        String path = a.path();
        if (router.hasRoute(path, Route.HttpMethod.PUT)) {
            throw new RuntimeException("Route already defined with path: " + path);
        }

        router.defineRoute(Route.put(path, createHandler(method)));
    }

    private void routeDelete(Delete a, Router router, Method method) {
        String path = a.path();
        if (router.hasRoute(path, Route.HttpMethod.DELETE)) {
            throw new RuntimeException("Route already defined with path: " + path);
        }

        router.defineRoute(Route.delete(path, createHandler(method)));
    }

    private void routePatch(Patch a, Router router, Method method) {
        String path = a.path();
        if (router.hasRoute(path, Route.HttpMethod.PATCH)) {
            throw new RuntimeException("Route already defined with path: " + path);
        }

        router.defineRoute(Route.patch(path, createHandler(method)));
    }

    private void routeHead(Head a, Router router, Method method) {
        String path = a.path();
        if (router.hasRoute(path, Route.HttpMethod.HEAD)) {
            throw new RuntimeException("Route already defined with path: " + path);
        }

        router.defineRoute(Route.head(path, createHandler(method)));
    }

    private void routeOptions(Options a, Router router, Method method) {
        String path = a.path();
        if (router.hasRoute(path, Route.HttpMethod.OPTIONS)) {
            throw new RuntimeException("Route already defined with path: " + path);
        }

        router.defineRoute(Route.options(path, createHandler(method)));
    }

    private Container container() {
        return KatanaCore.container;
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

