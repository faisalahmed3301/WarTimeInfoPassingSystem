package com.wartime.system.service;

import com.wartime.system.exception.UnauthorizedAccessException;
import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.UserFactory;
import com.wartime.system.security.SecurityContext;
import java.util.HashMap;
import java.util.Map;
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
        AbstractUser user = UserFactory.createUser(name, appointment);
        addUser(user, password);
    }

    private void addUser(AbstractUser user, String password) {
        users.put(user.getName(), user);
        userCredentials.put(user.getName(), password);
    }

    private boolean authenticate(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private AbstractUser getUser(String username) {
        return users.get(username);
    }
}
