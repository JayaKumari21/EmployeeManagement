package com.employeeManagement.EmployeeManagement.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Employee {

    @Id
    private Integer empId;
    private String empName;
    private String designation;
    private String location;
    private Integer salary;


}
