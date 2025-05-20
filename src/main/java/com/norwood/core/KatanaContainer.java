package com.norwood.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KatanaContainer implements Container {
    private Map<Class<?>, Object> beans = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> beanClass) {
        return (T) beans.get(beanClass);
    }

    @Override
    public <T> void set(Class<T> beanClass, T bean) {
        if (beans.get(beanClass) != null) {
            // Ignore subsequent registrations for singletons
            if (beanClass.isAnnotationPresent(Singleton.class)) {
                return;
            }
            throw new BeanAlreadyDefinedException("Bean already defined.");
        }

        beans.put(beanClass, bean);
    }

    @Override
    public List<Class<?>> classDefinitions() {
        return new ArrayList<>(beans.keySet());
    }
}

