package com.norwood.core;

import com.norwood.networking.KatanaServer;
import com.norwood.routing.Router;
import com.norwood.util.KatanaClient;

public class KatanaCore
{
    private static final Container container = new KatanaContainer();

    public void boot() {
        System.out.println("Booting Katana");
        (new Thread(() -> KatanaServer.getInstance())).start();
        System.out.println("Katana booted. Awaiting connections form clients...");

        try {
            registerMainBeans();
        } catch (ContainerException e) {
            throw new RuntimeException("Error initializing container exception.");
        }
    }

    private void registerMainBeans() throws ContainerException {
        Container container = getContainer();

        Router router = new Router();
        router.defineDefaultRoutes();
        container.set(Router.class, router);
    }

    public static Container getContainer() {
        return container;
    }
}
