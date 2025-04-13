package com.norwood.core;

import java.util.List;

public interface Container {
    public <T> T get(Class<T> beanClass);
    public <T> void set(Class<T> beanClass, T bean);
    public List<Class<?>> classDefinitions();
}

