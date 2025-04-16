package com.norwood.userland;

import java.util.List;

import com.norwood.core.KatanaCore;
import com.norwood.core.UserlandBeanRegistry;

public class UserBeanRegistry implements UserlandBeanRegistry {
    @SuppressWarnings("unchecked")
    public <T> void registerBeans() {
        List.of(
            (T) new UserController(),
            (T) new UserService(),
            (T) new Scraper()
        ).forEach(b -> KatanaCore.container.set(
            (Class<T>) b.getClass(),
            (T) b)
        ); 
    }
}
