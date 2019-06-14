package io.seventytwo.demo.hr.dto;

public class EmployeeDTO {

    private final String lastName;
    private final String firstName;
    private final Integer salary;

    public EmployeeDTO(String lastName, String firstName, Integer salary) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.salary = salary;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getSalary() {
        return salary;
    }
}
