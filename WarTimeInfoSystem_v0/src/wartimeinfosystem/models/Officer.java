package wartimeinfosystem.models;

import wartimeinfosystem.enums.Appointment;

/**
 * Officer class - inherits from MilitaryPersonnel
 */
public class Officer extends MilitaryPersonnel {
    private String department;
    private boolean isFieldOfficer;
    
    public Officer() {
        super();
    }
    
    public Officer(String name, String password, String serviceId, String department) {
        super(name, password, Appointment.OFFICER, serviceId);
        this.department = department;
        this.isFieldOfficer = false;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public boolean isFieldOfficer() {
        return isFieldOfficer;
    }
    
    public void setFieldOfficer(boolean fieldOfficer) {
        isFieldOfficer = fieldOfficer;
    }
    
    @Override
    public String getUserRole() {
        return "Officer - " + department;
    }
    
    public boolean canAccessOfficerMessages() {
        return true;
    }
}
