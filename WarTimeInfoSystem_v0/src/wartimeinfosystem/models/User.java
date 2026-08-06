package wartimeinfosystem.models;

import wartimeinfosystem.enums.Appointment;

/**
 * Base User class demonstrating encapsulation
 * This will be parent class for inheritance
 */
public class User {
    private String name;
    private String password;
    private Appointment appointment;
    private boolean authenticated;
    
    // Encapsulation: Constructor
    public User() {
        this.authenticated = false;
    }
    
    public User(String name, String password, Appointment appointment) {
        this.name = name;
        this.password = password;
        this.appointment = appointment;
        this.authenticated = false;
    }
    
    // Encapsulation: Getters
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    // Encapsulation: Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    // Method for polymorphism override
    public String getUserRole() {
        return "Basic User";
    }
    
    @Override
    public String toString() {
        return "User: " + name + " [" + appointment + "]";
    }
}
