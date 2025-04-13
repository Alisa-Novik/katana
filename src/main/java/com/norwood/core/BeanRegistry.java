package com.norwood.core;

import java.util.HashMap;
import java.util.Map;

import com.norwood.userland.UserController;

public class BeanRegistry {

    private static BeanRegistry instance = new BeanRegistry();

    public static final Map<Class<?>, Object> map = new HashMap<>();

    private static boolean init = false;

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> beanClass) {
        return (T) map.get(beanClass);
    }

    public <T> void set(Class<T> beanClass, T bean) {
        if (map.get(beanClass) != null) {
            throw new RuntimeException ("Bean already defined.");
        }
        map.put(beanClass, bean);
    }

    public static BeanRegistry instance() {
        if (!init) {
            init();
            init = true;
        }

        return instance;
    }

    private static void init() {
        instance.set(UserController.class, new UserController());
    }
}
