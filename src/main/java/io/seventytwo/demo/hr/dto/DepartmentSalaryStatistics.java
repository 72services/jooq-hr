package io.seventytwo.demo.hr.dto;

public class DepartmentSalaryStatistics {

    private final String departmentName;
    private final double avgSalary;

    public DepartmentSalaryStatistics(String departmentName, double avgSalary) {
        this.departmentName = departmentName;
        this.avgSalary = avgSalary;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public double getAvgSalary() {
        return avgSalary;
    }
}
