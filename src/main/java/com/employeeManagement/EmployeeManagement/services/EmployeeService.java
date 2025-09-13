package com.employeeManagement.EmployeeManagement.services;


import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    ResponseEntity<EmployeeDto> getAllEmployees();

    String addEmployee();
    List<EmployeeDto> getAllEmployee();
    EmployeeDto getEmployeeById(int empId);
    String deleteEmployee(int empId);

}
