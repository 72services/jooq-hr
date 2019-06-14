package io.seventytwo.demo.hr.dto;

public class EmployeeWithAddressDTO {

    private final String employeeLastName;
    private final String employeeFirstName;
    private final String addressStreet;
    private final String addressZip;
    private final String addressCity;

    public EmployeeWithAddressDTO(String employeeLastName, String employeeFirstName, String addressStreet, String addressZip, String addressCity) {
        this.employeeLastName = employeeLastName;
        this.employeeFirstName = employeeFirstName;
        this.addressStreet = addressStreet;
        this.addressZip = addressZip;
        this.addressCity = addressCity;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public String getAddressCity() {
        return addressCity;
    }
}
