package com.wartime.system.security;

import com.wartime.system.model.AbstractUser;

public class SecurityContext {
    private static SecurityContext instance;
    private AbstractUser currentUser;

    private SecurityContext() {
    }

    public static synchronized SecurityContext getInstance() {
        if (instance == null) {
            instance = new SecurityContext();
        }
        return instance;
    }

    public void setCurrentUser(AbstractUser user) {
        this.currentUser = user;
    }

    public AbstractUser getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }
}
