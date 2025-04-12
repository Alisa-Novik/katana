package com.norwood.userland;

import java.util.List;

import com.norwood.core.KatanaCore;

public class BeanRegistry {
    // private static final BeanRegistry instance = new BeanRegistry();
    private static boolean init = false;

    // public BeanRegistry instance() {
    //     if (!init) {
    //         init();
    //         init = true;
    //     }
    //
    // }
    //     return instance;

    public static void init() {
        if (init) {
            throw new RuntimeException("Already initialized registry.");
        }

        for (Class<?> userClass : beans()) {
            KatanaCore.beanRegistry.add(userClass);
        }

        init = true;
    }

    private static List<Class<?>> beans() {
        return List.of(
            UserRouter.class
        );
    }
}
