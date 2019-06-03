package io.seventytwo.demo.hr.dto;

public class EmployeeWithAddressDTO {

    private final String employeeName;
    private final String addressStreet;
    private final String addressZip;
    private final String addressCity;

    public EmployeeWithAddressDTO(String employeeName, String addressStreet, String addressZip, String addressCity) {
        this.employeeName = employeeName;
        this.addressStreet = addressStreet;
        this.addressZip = addressZip;
        this.addressCity = addressCity;
    }

    public String getEmployeeName() {
        return employeeName;
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
