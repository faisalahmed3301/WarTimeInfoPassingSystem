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

    private boolean lightMode = false;

    public boolean isLightMode() {
        return lightMode;
    }

    public void setLightMode(boolean lightMode) {
        this.lightMode = lightMode;
    }
}
