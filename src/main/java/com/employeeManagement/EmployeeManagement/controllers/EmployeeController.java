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
import java.util.Map;

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
    public ResponseEntity<List<EmployeeDto>> getAll(
            @RequestParam(value = "sortBy", defaultValue = "empId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        System.out.println("Value of sortBy in controller : " + sortBy);
        List<EmployeeDto> employeeDtoList = employeeService.getAllEmployee(sortBy, sortDir);
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
    

    // 5 Put :- Full
    @PutMapping("/{empId}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Integer empId, @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(empId, employeeDto);
        return ResponseEntity.ok(updatedEmployee);

    }
}
