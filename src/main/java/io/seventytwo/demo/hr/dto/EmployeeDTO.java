package io.seventytwo.demo.hr.dto;

public class EmployeeDTO {

    private final String name;
    private final String street;
    private final String zip;
    private final String city;
    private final String state;

    public EmployeeDTO(String name, String street, String zip, String city, String state) {
        this.name = name;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
