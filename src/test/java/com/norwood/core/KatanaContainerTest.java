package com.norwood.core;

import junit.framework.TestCase;

public class KatanaContainerTest extends TestCase {
    public void testSetAndGet() {
        KatanaContainer container = new KatanaContainer();
        container.set(String.class, "hello");
        assertEquals("hello", container.get(String.class));
    }

    public void testSetTwiceThrows() {
        KatanaContainer container = new KatanaContainer();
        container.set(Integer.class, 1);
        try {
            container.set(Integer.class, 2);
            fail("Expected ContainerException");
        } catch (ContainerException e) {
            // expected
        }
    }
}
