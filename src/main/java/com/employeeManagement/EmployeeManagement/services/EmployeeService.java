package com.employeeManagement.EmployeeManagement.services;


import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    EmployeeDto addEmployee();

    List<EmployeeDto> getAllEmployee(String sortBy, String sortDir, int pageNo, int pageSize);


    EmployeeDto getEmployeeById(int empId);

    String deleteEmployee(int empId);


//    ResponseEntity<EmployeeDto> updatePartialEmployee(Integer empId, Map<String, Object> updates);

    EmployeeDto updateEmployee(Integer empId, EmployeeDto employeeDto);

    EmployeeDto updatePartialEmployee(Integer empId, Map<String, Object> updates);
}
