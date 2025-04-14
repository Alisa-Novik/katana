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

public class AnnotationProcessor {
    public void processAnnotations(List<Class<?>> classDefinitions, Router router) {
        for (Class<?> userClass : classDefinitions) {
            for (Field field : userClass.getDeclaredFields()) {
                for (Annotation an : field.getDeclaredAnnotations()) {
                    System.out.println("Anno: " + an.getClass());
                    switch (an) {
                        case Inject _ -> inject(field);
                        default -> noop();
                    }
                }
            }
            for (Method method : userClass.getMethods()) {
                for (Annotation an : method.getAnnotations()) {
                    switch (an) {
                        case Get a -> routeGet(a, router, method);
                        case Post a -> routePost(a, router, method);
                        default -> noop();
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

    private void noop() {}

    private void routePost(Post a, Router router, Method method) {
        String path = a.path();
        if (router.hasRouteWithPath(path)) {
            throw new RuntimeException("Route already define with path: " + path);
        }

        router.defineRoute(Route.post(path, createHandler(method)));
    }

    private void routeGet(Get a, Router router, Method method) {
        String path = a.path();
        if (router.hasRouteWithPath(path)) {
            throw new RuntimeException("Route already define with path: " + path);
        }

        router.defineRoute(Route.get(path, createHandler(method)));
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

