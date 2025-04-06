package com.norwood.core;

public class BeanAlreadyDefinedException extends ContainerException {
    BeanAlreadyDefinedException(String message) {
        super(message);
    }
}

