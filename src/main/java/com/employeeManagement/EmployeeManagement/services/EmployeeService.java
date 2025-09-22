package com.employeeManagement.EmployeeManagement.services;


import com.employeeManagement.EmployeeManagement.dto.requests.EmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.requests.QueryParamsDto;
import com.employeeManagement.EmployeeManagement.dto.responses.EmployeeResponseDto;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    EmployeeResponseDto addEmployee(EmployeeDto employeeDto);

    List<EmployeeResponseDto> getAllEmployee(QueryParamsDto queryParamsDto);


    EmployeeResponseDto getEmployeeById(Integer empId);

    String deleteEmployee(Integer empId);


//    ResponseEntity<EmployeeDto> updatePartialEmployee(Integer empId, Map<String, Object> updates);

    EmployeeDto updateEmployee(Integer empId, EmployeeDto employeeDto);

    EmployeeDto updatePartialEmployee(Integer empId, Map<String, Object> updates);
}
