package com.employeeManagement.EmployeeManagement.dto.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateEmployeeDto {


    private String location;
    private String designation;
    private String empName;

    @Min(value = 0, message = "Salary must be positive")
    private Integer salary;
}
