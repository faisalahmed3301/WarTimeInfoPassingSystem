package com.wartime.system.model;

public class UserFactory {
    
    public static AbstractUser createUser(String name, String appointment) {
        if (appointment == null) return null;
        
        switch (appointment.toUpperCase()) {
            case "COMMANDER":
                return new Commander(name);
            case "OFFICER":
                return new Officer(name);
            case "SOLDIER":
                return new Soldier(name);
            default:
                throw new IllegalArgumentException("Unknown appointment: " + appointment);
        }
    }
}
