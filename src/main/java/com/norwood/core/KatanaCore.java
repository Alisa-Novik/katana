package com.norwood.core;

import java.net.http.HttpRequest;

import com.norwood.networking.KatanaServer;
import com.norwood.routing.Router;

public class KatanaCore {
    public static final Container container = new KatanaContainer();
    public static final AnnotationProcessor annotationProcessor = new AnnotationProcessor();
    private Router router = new Router();
    private ConfigManager configManager = new FileConfigManager();

    public void boot() {
        startServer();
        loadBeans();
        processAnnotations();
    }

    private void processAnnotations() {
        annotationProcessor.processAnnotations(container.classDefinitions(), router);
    }

    private void loadBeans() {
        container.set(Router.class, router);
        container.set(ConfigManager.class, configManager);

        RegistryLoader.load();
    }

    private void startServer() {
        (new Thread(() -> KatanaServer.withCore(this))).start();
    }

    public KatanaResponse handleRequest(HttpRequest req) {
        try {
            return KatanaResponse.success(router.route(req));
        } catch (Exception e) {
            return KatanaResponse.error("Katana response processing error");
        }
    }
}
