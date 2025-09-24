package com.employeeManagement.EmployeeManagement.dto.responses;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Data
// getter and setter
public class EmployeeResponseDto {

    private Integer empId;
    private String empName;
    private String designation;
    private String location;
    private Integer salary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
