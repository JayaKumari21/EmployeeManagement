package com.employeeManagement.EmployeeManagement.controllers;

import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.services.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    // 1 Add employee
   @PostMapping("/add")
    public String addEmployee(@RequestBody EmployeeDto employeeDto){
       return employeeService.addEmployee(employeeDto);

   }

    //2 Get all employee
    @GetMapping("getAll")
    public List<EmployeeDto> getAll(){
       return employeeService.getAllEmployee();
    }

    //3 Get Employee by id
    @GetMapping("/getById/{empId}")
    public EmployeeDto getEmployee(@PathVariable int empId){
       return employeeService.getEmployeeById(empId);
    }

    //4 Delete Employee by id
    @DeleteMapping("/delete/{empId}")
    public String deleteEmployee(@PathVariable int empId){
       return employeeService.deleteEmployeeById(empId);
    }

    //5 Update single





    

}
