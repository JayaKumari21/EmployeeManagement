package com.employeeManagement.EmployeeManagement.services.impl;

import com.employeeManagement.EmployeeManagement.dto.requests.EmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.requests.QueryParamsDto;
import com.employeeManagement.EmployeeManagement.dto.requests.UpdateEmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.responses.EmployeeResponseDto;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.repositories.EmployeeRepository;
import com.employeeManagement.EmployeeManagement.specifications.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
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

    //find employee by id
    private Employee findEmployeeById(Integer empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id : " + empId + " is not found"));

    }

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
    public List<EmployeeResponseDto> getAllEmployee(QueryParamsDto queryParamsDto) throws Exception {
        if (queryParamsDto.getStartTime() == null) {
            if (queryParamsDto.getEndTime() != null) {
                throw new Exception("Start time is missing");
            }
        } else {
            if (queryParamsDto.getEndTime() == null) {
                throw new Exception("End time is missing");
            }

        }
        Pageable pageable = buildPageableObj(queryParamsDto);
//        Specification<Employee> specification = Specification.anyOf(
//                EmployeeSpecification.hasDesignation(queryParamsDto.getDesignation()),
//                EmployeeSpecification.hasLocation((queryParamsDto.getLocation())));

        Specification<Employee> specification = Specification.allOf(
                EmployeeSpecification.hasField("location", queryParamsDto.getLocation()),
                EmployeeSpecification.hasField("designation", queryParamsDto.getDesignation()),
                EmployeeSpecification.containsPattern("empName", queryParamsDto.getEmpName()),
                EmployeeSpecification.greaterThanNum("salary", queryParamsDto.getSalary()),
                EmployeeSpecification.valueBetween("createdAt", queryParamsDto.getStartTime(), queryParamsDto.getEndTime()));


        // where location='mumbai'
        // where location ='mumnao or disc = sde'

        Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);

        List<Employee> employeeList = employeePage.getContent();

        return employeeList.stream()
                .map((e) -> modelMapper.map(e, EmployeeResponseDto.class))
                .collect(Collectors.toList());

    }

    //2 Get employee by id
    public EmployeeResponseDto getEmployeeById(Integer empId) {
        Employee employee = findEmployeeById(empId);
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
        findEmployeeById(empId);
        employeeRepository.deleteById(empId);
        return "Employee Deleted successfully";
    }


    //5 PUT full updation
    public EmployeeResponseDto updateEmployee(Integer empId, EmployeeDto employeeDto) {
        Employee existingEmployee = findEmployeeById(empId);
        modelMapper.map(employeeDto, existingEmployee);
//        Employee incomingEmployee = modelMapper.map(employeeDto, Employee.class);
//        incomingEmployee.setEmpId(existingEmployee.getEmpId());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return modelMapper.map(updatedEmployee, EmployeeResponseDto.class);
    }

    //6 PATCH
    public EmployeeResponseDto updatePartialEmployee(Integer empId, UpdateEmployeeDto updateEmployeeDto) {
        Employee existingEmployee = findEmployeeById(empId);
        System.out.println("Existing emp name " + existingEmployee.getEmpName());

        modelMapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(updateEmployeeDto, existingEmployee);

        Employee partialUpdatedEmployee = employeeRepository.save(existingEmployee);
        System.out.println("Updated name : " + partialUpdatedEmployee.getEmpName());
        return modelMapper.map(partialUpdatedEmployee, EmployeeResponseDto.class);
    }


}

