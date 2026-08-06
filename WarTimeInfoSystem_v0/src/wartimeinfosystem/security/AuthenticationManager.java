package wartimeinfosystem.security;

import wartimeinfosystem.models.User;
import wartimeinfosystem.interfaces.Authenticatable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages user authentication
 * Demonstrates encapsulation
 */
public class AuthenticationManager implements Authenticatable {
    private List<User> registeredUsers;
    private User currentUser;
    private boolean authorized;
    
    public AuthenticationManager() {
        this.registeredUsers = new ArrayList<>();
        this.authorized = false;
        initializeDefaultUsers();
    }
    
    private void initializeDefaultUsers() {
        // Pre-registered users for testing
        // In real scenario, this would come from a secure source
    }
    
    public void registerUser(User user) {
        if (user != null) {
            registeredUsers.add(user);
        }
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        for (User user : registeredUsers) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                currentUser.setAuthenticated(true);
                authorized = true;
                return true;
            }
        }
        authorized = false;
        return false;
    }
    
    @Override
    public boolean isAuthorized() {
        return authorized;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        if (currentUser != null) {
            currentUser.setAuthenticated(false);
        }
        currentUser = null;
        authorized = false;
    }
    
    public List<User> getRegisteredUsers() {
        return new ArrayList<>(registeredUsers);
    }
}
