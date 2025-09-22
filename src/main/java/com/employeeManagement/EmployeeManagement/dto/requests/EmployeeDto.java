package com.employeeManagement.EmployeeManagement.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDto {

    @NotBlank(message = "Name is mandatory")
    private String empName;

    @NotBlank(message = "Designation is mandatory")
    private String designation;

    @NotBlank(message = "Location is mandatory")
    private String location;

    @Min(value = 0, message = "Salary must be positive")
    private int salary;
}