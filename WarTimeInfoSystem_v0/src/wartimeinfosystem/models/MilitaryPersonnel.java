package wartimeinfosystem.models;

import wartimeinfosystem.enums.Appointment;

/**
 * MilitaryPersonnel class - inherits from User
 * Demonstrates inheritance and polymorphism
 */
public class MilitaryPersonnel extends User {
    private String serviceId;
    private int yearsOfService;
    
    public MilitaryPersonnel() {
        super();
    }
    
    public MilitaryPersonnel(String name, String password, Appointment appointment, String serviceId) {
        super(name, password, appointment);
        this.serviceId = serviceId;
        this.yearsOfService = 0;
    }
    
    // Encapsulation
    public String getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    
    public int getYearsOfService() {
        return yearsOfService;
    }
    
    public void setYearsOfService(int yearsOfService) {
        this.yearsOfService = yearsOfService;
    }
    
    // Polymorphism: Override parent method
    @Override
    public String getUserRole() {
        return "Military Personnel - " + getAppointment();
    }
}
