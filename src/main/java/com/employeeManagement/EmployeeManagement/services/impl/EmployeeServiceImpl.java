package com.employeeManagement.EmployeeManagement.services.impl;

import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.exceptions.DuplicateEmployeeException;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.repositories.EmployeeRepository;
import com.employeeManagement.EmployeeManagement.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sound.midi.Soundbank;
import java.security.Key;
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
    public List<EmployeeDto> getAllEmployee(String sortBy, String sortDir, int pageNo, int pageSize) {
        List<Employee> employeeList;
        if (sortBy != null) {
//            try to do using enum
            List<String> allowedFields = List.of("empId", "empName", "salary");
            if (!allowedFields.contains(sortBy)) {
                throw new IllegalArgumentException("Invalid sort field for sorting :" + sortBy);
            }
            Sort.Direction direction = (sortDir.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            Page<Employee> employeePage = employeeRepository.findAll(pageable);
            employeeList = employeePage.getContent();
            System.out.println("Sorting object create" + sort.toString().getClass());

        } else {
            //without sorting+pagination
            employeeList = employeeRepository.findAll();
        }

        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee e : employeeList) {
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

    //6 PATCH
    public EmployeeDto updatePartialEmployee(Integer empId, Map<String, Object> updates) {
        Employee existingEmployee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + empId + " is not found"));
        System.out.println("Existing emp name " + existingEmployee.getEmpName());
        updates.forEach((key, value) -> {
            if (key.equalsIgnoreCase("empName")) {
                existingEmployee.setEmpName((String) value);
            }
            if (key.equalsIgnoreCase("designation")) {
                existingEmployee.setDesignation((String) value);
            }
            if (key.equalsIgnoreCase("location")) {
                existingEmployee.setLocation((String) value);
            }
            if (key.equalsIgnoreCase("salary")) {
                existingEmployee.setSalary((Integer) value);
            }

        });
        Employee partialUpdatedEmployee = employeeRepository.save(existingEmployee);
        System.out.println("Updated name : " + partialUpdatedEmployee.getEmpName());
        return modelMapper.map(partialUpdatedEmployee, EmployeeDto.class);
    }


}

