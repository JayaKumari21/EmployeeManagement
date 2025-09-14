package com.employeeManagement.EmployeeManagement.controllers;

import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.services.impl.EmployeeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

//    @Autowired
//    private EmployeeServiceImpl employeeService;

    // Constructor injection
    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    // 1 Add employee
    @PostMapping()
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto savedEmployee = employeeService.addEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);

    }

    //2 Get all employee
    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAll() {
        List<EmployeeDto> employeeDtoList = employeeService.getAllEmployee();
        return new ResponseEntity<>(employeeDtoList, HttpStatus.OK);
    }

    //3 Get Employee by id
    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable int empId) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(empId);
        return ResponseEntity.ok(employeeDto);
    }

    //4 Delete Employee by id
    @DeleteMapping("/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int empId) {
        return new ResponseEntity<>(employeeService.deleteEmployeeById(empId), HttpStatus.OK);
    }
//-------------------------------------------------------------------------------------

    //5 Get Sorted Employee on id basis ASC/DESC
    @GetMapping("/sortedById")
    public List<EmployeeDto> getSortedEmployeesById(@RequestParam String sortDir) {
        return employeeService.fetchSortedEmployeesById(sortDir);

    }

    //6 Get sorted employee based on any field
    @GetMapping("sorted")
    public List<EmployeeDto> getSortedEmployees(@RequestParam String sortBy, @RequestParam String sortDir) {
        return employeeService.fetchSortedEmployeesByAnyField(sortBy, sortDir);
    }


}
