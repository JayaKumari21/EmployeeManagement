package com.employeeManagement.EmployeeManagement.services.impl;

import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.exceptions.DuplicateEmployeeException;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.repositories.EmployeeRepository;
import com.employeeManagement.EmployeeManagement.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

//    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }


    //1 Adding/inserting
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        if (employeeRepository.findById(employeeDto.getEmpId()).isPresent()) {
            throw new DuplicateEmployeeException("Employee with id " + employeeDto.getEmpId() + " already present");
        }
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    // 2 Get all employee
    public List<EmployeeDto> getAllEmployee(String sortBy, String sortDir) {
        List<String> allowedFields = List.of("empId", "empName", "salary");
        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field for sorting :" + sortBy);
        }
        Sort.Direction direction = (sortDir.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Sort.Direction direction =Sort.Direction.ASC;
//        if (sortDir.equalsIgnoreCase("desc")) {
//            direction = Sort.Direction.DESC;
//        }
        Sort sort = Sort.by(direction, sortBy);
        System.out.println("Sorting object create" + sort.toString().getClass());
        List<Employee> list = employeeRepository.findAll(sort);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No employee found : Empty Table");
        }
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee e : list) {
            employeeDtoList.add(modelMapper.map(e, EmployeeDto.class));
        }
        return employeeDtoList;
    }

    // 3 Get employee by id
    public EmployeeDto getEmployeeById(Integer empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + empId + " is not found"));
        return modelMapper.map(employee, EmployeeDto.class);
    }


    //4 Delete Employee by id
    public String deleteEmployeeById(Integer empId) {
        employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + empId + " is not found"));
        employeeRepository.deleteById(empId);
        return "Employee Deleted successfully";
    }


    //5 PUT full updation
    public EmployeeDto updateEmployee(Integer empId, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + empId + " is not found"));
        existingEmployee.setDesignation(employeeDto.getDesignation());
        existingEmployee.setLocation(employeeDto.getLocation());
        existingEmployee.setSalary(employeeDto.getSalary());
        existingEmployee.setEmpName(employeeDto.getEmpName());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }

}

