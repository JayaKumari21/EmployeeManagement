package com.employeeManagement.EmployeeManagement.dto.responses;

import lombok.Data;

import java.util.Date;

@Data
// getter and setter
public class EmployeeResponseDto {

    private Integer empId;
    private String empName;
    private String designation;
    private String location;
    private Integer salary;
    private Date createdAt;
    private Date updatedAt;

}
