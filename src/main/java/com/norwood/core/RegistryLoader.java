package com.norwood.core;

import java.lang.reflect.InvocationTargetException;

public class RegistryLoader {
    static void load() {
        try {
            Class<?> clazz = Class.forName(resolveRegistryFqn());
            UserlandBeanRegistry reg = (UserlandBeanRegistry) clazz.getDeclaredConstructor().newInstance();
            reg.registerBeans();
        } catch (ClassNotFoundException | InstantiationException  | 
                IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to load user-defined beans.");
        }
    }

    static String resolveRegistryFqn() {
        ConfigManager manager = KatanaCore.container.get(ConfigManager.class);
        return manager.get("beanRegistryClass");
    }
}

