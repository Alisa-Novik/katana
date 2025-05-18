package com.norwood.userland;

import com.norwood.core.AnnotationProcessor;
import com.norwood.core.KatanaCore;
import com.norwood.routing.Router;
import junit.framework.TestCase;

public class AnnotationProcessorTest extends TestCase {
    public void testProcessAnnotationsInjectsAndRegistersRoutes() {
        if (KatanaCore.container.get(UserController.class) == null) {
            KatanaCore.container.set(UserController.class, new UserController());
        }

        AnnotationProcessor ap = new AnnotationProcessor();
        Router router = new Router();

        ap.processAnnotations(KatanaCore.container.classDefinitions(), router);

        UserController ctrl = KatanaCore.container.get(UserController.class);
        assertNotNull(ctrl.userService);
        assertNotNull(ctrl.scraper);
        assertTrue(router.hasRouteWithPath("/test1"));
        assertTrue(router.hasRouteWithPath("/test2"));
        assertTrue(router.hasRouteWithPath("/test3"));
        assertTrue(router.hasRouteWithPath("/scrape"));
    }
}
