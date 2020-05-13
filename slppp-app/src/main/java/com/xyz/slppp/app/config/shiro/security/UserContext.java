package com.xyz.slppp.app.config.shiro.security;


import com.xyz.slppp.app.config.shiro.LoginUser;

public class UserContext implements AutoCloseable {

    static final ThreadLocal<LoginUser> current = new ThreadLocal<>();

    public UserContext(LoginUser user) {
        current.set(user);
    }

    public static LoginUser getCurrentUser() {
        return current.get();
    }

    public static void setCurrentUser(LoginUser user) {
        current.set(user);
    }

    public void close() {
        current.remove();
    }
}
