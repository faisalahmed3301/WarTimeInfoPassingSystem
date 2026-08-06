package com.wartime.system.service;

import com.wartime.system.exception.UnauthorizedAccessException;
import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.UserFactory;
import com.wartime.system.security.SecurityContext;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private static AuthenticationService instance;
    private Map<String, AbstractUser> users = new HashMap<>();
    private Map<String, String> userCredentials = new HashMap<>();

    private AuthenticationService() {
        // kono kaj nei
    }

    public static synchronized AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public void login(String username, String appointment, String password) {
        if (username == null || password == null || appointment == null) {
            throw new UnauthorizedAccessException("Username, password, and appointment cannot be null.");
        }

        if (authenticate(username, password)) {
            AbstractUser user = getUser(username);

            if (!user.getRank().name().equalsIgnoreCase(appointment)) {
                throw new UnauthorizedAccessException("Appointment mismatch.");
            }
            SecurityContext.getInstance().setCurrentUser(user);
        } else {
            throw new UnauthorizedAccessException("Invalid Credentials");
        }
    }

    public void register(String name, String appointment, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        registerInternal(name, appointment, password);
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    public void registerInternal(String name, String appointment, String password) {
        AbstractUser user = UserFactory.createUser(name, appointment);
        addUser(user, password);
    }

    public void addUser(AbstractUser user, String password) {
        users.put(user.getName(), user);
        userCredentials.put(user.getName(), password);
    }

    private boolean authenticate(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    public AbstractUser getUser(String username) {
        return users.get(username);
    }

    public Map<String, AbstractUser> getUsers() {
        return users;
    }

    public Map<String, String> getUserCredentials() {
        return userCredentials;
    }
}
