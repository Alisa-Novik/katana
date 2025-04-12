package com.norwood.core;

import java.lang.annotation.Annotation;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import com.norwood.networking.KatanaServer;
import com.norwood.routing.Router;
import com.norwood.userland.BeanRegistry;

public class KatanaCore
{
    private static final Container container = new KatanaContainer();
    public static final Set<Class<?>> beanRegistry = new HashSet<>();

    public void boot() {
        System.out.println("Booting Katana");
        (new Thread(() -> KatanaServer.getInstance())).start();
        System.out.println("Katana booted. Awaiting connections form clients...");

        try {
            registerMainBeans();
        } catch (ContainerException e) {
            throw new RuntimeException("Error initializing container exception.");
        }

        BeanRegistry.init();
        processAutowiring();
        }

    private void registerMainBeans() throws ContainerException {
        Container container = getContainer();

        Router router = new Router();
        router.defineDefaultRoutes();
        container.set(Router.class, router);

        processAutowiring();
    }

    private void processAutowiring() {
        for (Class<?> userClass : beanRegistry) {

            System.out.println(String.format("Class %s annotations", userClass.toString()));

            for (Annotation annotation : userClass.getAnnotations()) {
                System.out.println("Annotation: " + annotation);
            }
        }
    }

    public static Container getContainer() {
        return container;
    }
}
