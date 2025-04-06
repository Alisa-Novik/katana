package com.norwood.core;

public interface Container {
    public <T extends KatanaBean> T get(Class<T> beanClass);
    public <T extends KatanaBean> void set(Class<T> beanClass, T bean) throws BeanAlreadyDefinedException;
}

