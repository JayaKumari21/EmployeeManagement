package com.employeeManagement.EmployeeManagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDto {
    private int empId;
    private String empName;
    private String designation;
    private String location;
    private int salary;
}