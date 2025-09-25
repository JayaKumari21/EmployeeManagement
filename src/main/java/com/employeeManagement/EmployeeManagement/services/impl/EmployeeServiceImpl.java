package com.employeeManagement.EmployeeManagement.services.impl;

import com.employeeManagement.EmployeeManagement.dto.requests.EmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.requests.QueryParamsDto;
import com.employeeManagement.EmployeeManagement.dto.responses.EmployeeResponseDto;
import com.employeeManagement.EmployeeManagement.exceptions.DuplicateEmployeeException;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.repositories.EmployeeRepository;
import com.employeeManagement.EmployeeManagement.specifications.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;


    //Create Pageable object
    private Pageable buildPageableObj(QueryParamsDto queryParamsDto) {

        if (queryParamsDto.getSortBy() != null) {
            Sort sort = Sort.by(queryParamsDto.getSortDir() == null ?
                            Sort.Direction.ASC :
                            Sort.Direction.valueOf(queryParamsDto.getSortDir().name()),
                    queryParamsDto.getSortBy().getFieldName()
            );
            Pageable pageable = PageRequest.of(
                    queryParamsDto.getPageNo(),
                    queryParamsDto.getPageSize(), sort
            );
            return pageable;
        }
        return PageRequest.of(queryParamsDto.getPageNo(),
                queryParamsDto.getPageSize());
    }


    // 1 Get all employee
    public List<EmployeeResponseDto> getAllEmployee(QueryParamsDto queryParamsDto) {
        Pageable pageable = buildPageableObj(queryParamsDto);
//        Specification<Employee> specification = Specification.anyOf(
//                EmployeeSpecification.hasDesignation(queryParamsDto.getDesignation()),
//                EmployeeSpecification.hasLocation((queryParamsDto.getLocation())));

        Specification<Employee> specification = Specification.allOf(
                EmployeeSpecification.hasField("location", queryParamsDto.getLocation()),
                EmployeeSpecification.hasField("designation", queryParamsDto.getDesignation()));


        Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);

        List<Employee> employeeList = employeePage.getContent();

        return employeeList.stream()
                .map((e) -> modelMapper.map(e, EmployeeResponseDto.class))
                .collect(Collectors.toList());

    }

    //2 Get employee by id
    public EmployeeResponseDto getEmployeeById(Integer empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + empId + " is not found"));
        return modelMapper.map(employee, EmployeeResponseDto.class);
    }

    //3 Adding/inserting
    public EmployeeResponseDto addEmployee(EmployeeDto employeeDto) {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeResponseDto.class);
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

