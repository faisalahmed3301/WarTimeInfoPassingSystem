package wartimeinfosystem.models;

import wartimeinfosystem.enums.Appointment;

/**
 * Soldier class - inherits from MilitaryPersonnel
 */
public class Soldier extends MilitaryPersonnel {
    private String squad;
    private String specialization;
    
    public Soldier() {
        super();
    }
    
    public Soldier(String name, String password, String serviceId, String squad) {
        super(name, password, Appointment.SOLDIER, serviceId);
        this.squad = squad;
        this.specialization = "Infantry";
    }
    
    public String getSquad() {
        return squad;
    }
    
    public void setSquad(String squad) {
        this.squad = squad;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    @Override
    public String getUserRole() {
        return "Soldier - " + specialization;
    }
}
