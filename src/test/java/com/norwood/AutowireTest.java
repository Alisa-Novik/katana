package com.norwood;

import junit.framework.TestCase;

import com.norwood.core.AnnotationProcessor;
import com.norwood.core.KatanaContainer;
import com.norwood.core.Singleton;
import com.norwood.core.annotations.Inject;
import com.norwood.routing.Router;

public class AutowireTest extends TestCase {
    @Singleton
    public static class SingletonService {}

    public static class NoDefaultConstructor {
        final SingletonService service;
        public NoDefaultConstructor(SingletonService service) {
            this.service = service;
        }
    }

    public static class ClientBean {
        @Inject SingletonService singleton;
        @Inject NoDefaultConstructor nodefault;
    }

    public void testAutowiring() {
        KatanaContainer container = new KatanaContainer();
        container.set(ClientBean.class, new ClientBean());
        container.set(SingletonService.class, new SingletonService());

        AnnotationProcessor processor = new AnnotationProcessor();
        processor.processAnnotations(container.classDefinitions(), new Router());

        ClientBean bean = container.get(ClientBean.class);
        SingletonService svc = container.get(SingletonService.class);

        assertSame(svc, bean.singleton);
        assertNotNull(bean.nodefault);
        assertSame(svc, bean.nodefault.service);
    }
}
