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

        getStarted();

        container.set(Router.class, router);
        container.set(ConfigManager.class, configManager);

        RegistryLoader.load();

        annotationProcessor.processAnnotations(container.classDefinitions(), router);
    }

    private void getStarted() {
        (new Thread(() -> KatanaServer.withCore(this))).start();
    }

    public KatanaResponse handleRequest(HttpRequest req) {
        try {
            Object result = router.route(req);
            if (result == null) {
                return KatanaResponse.error("Not Found");
            }
            return KatanaResponse.success(result);
        } catch (Exception e) {
            return KatanaResponse.error("Katana response processing error");
        }
    }
}
