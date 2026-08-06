package wartimeinfosystem.enums;

/**
 * Enum representing military appointments/ranks
 * Demonstrates encapsulation with hierarchical ordering
 */
public enum Appointment {
    COMMANDER(3, "Commander"),
    OFFICER(2, "Officer"),
    SOLDIER(1, "Soldier");
    
    private final int level;
    private final String displayName;
    
    // Encapsulation: private constructor
    private Appointment(int level, String displayName) {
        this.level = level;
        this.displayName = displayName;
    }
    
    // Encapsulation: getter methods
    public int getLevel() {
        return level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    // Check if this appointment can read messages from sender's appointment
    public boolean canReadMessage(Appointment senderAppointment) {
        return this.level >= senderAppointment.level;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
