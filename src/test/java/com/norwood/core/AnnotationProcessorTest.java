package com.norwood.core;

import junit.framework.TestCase;

import java.util.List;

import com.norwood.core.annotations.Inject;
import com.norwood.routing.Router;

public class AnnotationProcessorTest extends TestCase {

    // simple dependency classes
    public static class ServiceA {}
    public static class ServiceB {}

    // class that requires injection
    public static class Consumer {
        @Inject public ServiceA a;
        @Inject public ServiceB b;
    }

    public void testInjectFields() {
        Container container = KatanaCore.container;

        // register instances
        ServiceA sa = new ServiceA();
        ServiceB sb = new ServiceB();
        Consumer consumer = new Consumer();
        container.set(ServiceA.class, sa);
        container.set(ServiceB.class, sb);
        container.set(Consumer.class, consumer);

        AnnotationProcessor proc = new AnnotationProcessor();
        proc.processAnnotations(List.of(Consumer.class), new Router());

        Consumer stored = container.get(Consumer.class);
        assertNotNull(stored.a);
        assertNotNull(stored.b);
        assertSame(sa, stored.a);
        assertSame(sb, stored.b);
    }
}
