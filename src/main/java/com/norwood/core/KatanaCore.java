package com.norwood.core;

import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.Set;

import com.norwood.networking.KatanaServer;
import com.norwood.routing.Router;

public class KatanaCore {
    private static final Container container = new KatanaContainer();
    public static final Set<Class<?>> beanRegistryDefinitions = new HashSet<>();
    public static final AnnotationProcessor annotationProcessor = new AnnotationProcessor();
    private Router router;

    public void boot() {
        System.out.println("Booting Katana");
        (new Thread(() -> KatanaServer.withCore(this))).start();
        System.out.println("Katana booted. Awaiting connections form clients...");

        try {
            container.set(Router.class, router);
        } catch (ContainerException e) {
            throw new RuntimeException("Error initializing container exception.");
        }

        System.out.println("Processing routes...");

        annotationProcessor.processAnnotations(beanRegistryDefinitions);
    }

    public KatanaResponse handleRequest(HttpRequest req) {
        try {
            Object responseValue = router.route(req);
            return KatanaResponse.some(responseValue);
        } catch (Exception e) {
            return KatanaResponse.error("Katana response processing error");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void registerBean(T bean) {
        beanRegistryDefinitions.add(bean.getClass());
        BeanRegistry.instance().set((Class<T>) bean.getClass(), bean);
    }
}
