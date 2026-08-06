package wartimeinfosystem.models;

import wartimeinfosystem.enums.Appointment;

/**
 * Commander class - inherits from MilitaryPersonnel
 * Demonstrates multi-level inheritance
 */
public class Commander extends MilitaryPersonnel {
    private String unitCommand;
    private int subordinates;
    
    public Commander() {
        super();
    }
    
    public Commander(String name, String password, String serviceId, String unitCommand) {
        super(name, password, Appointment.COMMANDER, serviceId);
        this.unitCommand = unitCommand;
        this.subordinates = 0;
    }
    
    public String getUnitCommand() {
        return unitCommand;
    }
    
    public void setUnitCommand(String unitCommand) {
        this.unitCommand = unitCommand;
    }
    
    public int getSubordinates() {
        return subordinates;
    }
    
    public void setSubordinates(int subordinates) {
        this.subordinates = subordinates;
    }
    
    // Polymorphism: Override
    @Override
    public String getUserRole() {
        return "Commander of " + unitCommand;
    }
    
    public boolean canAccessAllMessages() {
        return true;
    }
}
