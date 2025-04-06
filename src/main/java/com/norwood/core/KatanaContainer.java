package com.norwood.core;

import java.util.HashMap;
import java.util.Map;

public class KatanaContainer implements Container {
    private Map<Class<?>, KatanaBean> beans = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KatanaBean> T get(Class<T> beanClass) {
        return (T) beans.get(beanClass);
    }

    @Override
    public <T extends KatanaBean> void set(Class<T> beanClass, T bean) throws BeanAlreadyDefinedException {
        if (beans.get(beanClass) != null) {
            throw new BeanAlreadyDefinedException("Bean already defined.");
        }
        beans.put(beanClass, bean);
    }
}

