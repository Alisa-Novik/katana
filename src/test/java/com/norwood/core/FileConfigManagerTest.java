package com.norwood.core;

import junit.framework.TestCase;

public class FileConfigManagerTest extends TestCase {
    public void testGetReturnsConfigValue() {
        FileConfigManager manager = new FileConfigManager();
        String val = manager.get("beanRegistryClass");
        assertEquals("com.norwood.userland.UserBeanRegistry", val);
        // second call should return same value from cache
        assertEquals(val, manager.get("beanRegistryClass"));
    }
}
