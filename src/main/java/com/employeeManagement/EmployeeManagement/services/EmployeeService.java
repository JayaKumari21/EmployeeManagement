package com.employeeManagement.EmployeeManagement.services;


import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    ResponseEntity<EmployeeDto> addEmployee();

    ResponseEntity<List<EmployeeDto>> getAllEmployee();


    ResponseEntity<EmployeeDto> getEmployeeById(int empId);

    ResponseEntity<String> deleteEmployee(int empId);

    List<EmployeeDto> fetchSortedEmployeesById(String sortDir);

    List<EmployeeDto> fetchSortedEmployeesByAnyField(String sortBy, String sortDir);


}
