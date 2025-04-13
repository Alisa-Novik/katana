package com.norwood.core;

import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpRequest;

import com.norwood.networking.KatanaServer;
import com.norwood.routing.Router;

public class KatanaCore {
    public static final Container container = new KatanaContainer();
    public static final AnnotationProcessor annotationProcessor = new AnnotationProcessor();

    private Router router = new Router();
    private ConfigManager configManager = new FileConfigManager();

    public void boot() {
        System.out.println("Booting Katana");
        (new Thread(() -> KatanaServer.withCore(this))).start();
        System.out.println("Katana booted. Awaiting connections form clients...");

        container.set(Router.class, router);
        container.set(ConfigManager.class, configManager);
        userlandRegistry().registerBeans();

        System.out.println("Processing annotations...");
        annotationProcessor.processAnnotations(container.classDefinitions(), router);
    }

    private UserlandBeanRegistry userlandRegistry() {
        try {
            Class<?> clazz = Class.forName(configManager.get("beanRegistryClass"));
            return (UserlandBeanRegistry) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException  | 
                IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to load user-defined beans.");
        }
    }

    public KatanaResponse handleRequest(HttpRequest req) {
        try {
            Object responseValue = router.route(req);
            return KatanaResponse.success(responseValue);
        } catch (Exception e) {
            return KatanaResponse.error("Katana response processing error");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void registerBean(T bean) {
        BeanRegistry.instance().set((Class<T>) bean.getClass(), bean);
    }
}
