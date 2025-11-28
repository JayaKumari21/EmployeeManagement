package com.employeeManagement.EmployeeManagement.services.impl;

import com.employeeManagement.EmployeeManagement.dto.requests.EmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.requests.QueryParamsDto;
import com.employeeManagement.EmployeeManagement.dto.requests.UpdateEmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.responses.EmployeeResponseDto;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.repositories.EmployeeRepository;
import com.employeeManagement.EmployeeManagement.specifications.EmployeeSpecification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    // ------------------------------ Private Utility Methods ------------------------------

    private Employee findEmployeeById(Integer empId) {
        log.debug("Attempting to fetch employee with ID: {}", empId);

        return employeeRepository.findById(empId)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", empId);
                    return new ResourceNotFoundException("Employee with id : " + empId + " is not found");
                });
    }


    private Pageable buildPageableObj(QueryParamsDto queryParamsDto) {
        log.debug("Building pageable object for pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                queryParamsDto.getPageNo(), queryParamsDto.getPageSize(),
                queryParamsDto.getSortBy(), queryParamsDto.getSortDir());

        if (queryParamsDto.getSortBy() != null) {
            Sort sort = Sort.by(
                    queryParamsDto.getSortDir() == null ? Sort.Direction.ASC :
                            Sort.Direction.valueOf(queryParamsDto.getSortDir().name()),
                    queryParamsDto.getSortBy().getFieldName()
            );

            return PageRequest.of(
                    queryParamsDto.getPageNo(),
                    queryParamsDto.getPageSize(),
                    sort
            );
        }

        return PageRequest.of(queryParamsDto.getPageNo(), queryParamsDto.getPageSize());
    }


    // ------------------------------ Public Service Methods ------------------------------

    // 1. Get all employees
    public List<EmployeeResponseDto> getAllEmployee(QueryParamsDto queryParamsDto) throws Exception {
        log.info("Fetching employee list with filters: {}", queryParamsDto);
        log.debug("DEBUG log");

        if (queryParamsDto.getStartTime() == null && queryParamsDto.getEndTime() != null) {
            log.error("Start time missing while end time provided");
            throw new Exception("Start time is missing");
        }
        if (queryParamsDto.getStartTime() != null && queryParamsDto.getEndTime() == null) {
            log.error("End time missing while start time provided");
            throw new Exception("End time is missing");
        }

        Pageable pageable = buildPageableObj(queryParamsDto);

        Specification<Employee> specification = Specification.allOf(
                EmployeeSpecification.hasField("location", queryParamsDto.getLocation()),
                EmployeeSpecification.hasField("designation", queryParamsDto.getDesignation()),
                EmployeeSpecification.containsPattern("empName", queryParamsDto.getEmpName()),
                EmployeeSpecification.greaterThanNum("salary", queryParamsDto.getSalary()),
                EmployeeSpecification.valueBetween("createdAt", queryParamsDto.getStartTime(), queryParamsDto.getEndTime())
        );

        log.debug("Executing database query for employee page");

        Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);
        List<Employee> employeeList = employeePage.getContent();

        log.info("Employee records fetched successfully. Total records: {}", employeeList.size());

        return employeeList.stream()
                .map(e -> modelMapper.map(e, EmployeeResponseDto.class))
                .collect(Collectors.toList());
    }


    // 2. Get employee by ID
    public EmployeeResponseDto getEmployeeById(Integer empId) {
        log.info("Fetching employee by ID: {}", empId);

        Employee employee = findEmployeeById(empId);
            log.debug("Successfully fetched employee with ID: {}", empId);

        return modelMapper.map(employee, EmployeeResponseDto.class);
    }


    // 3. Add employee
    public EmployeeResponseDto addEmployee(EmployeeDto employeeDto) {
        log.info("Adding new employee");

        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee added successfully with ID: {}", savedEmployee.getEmpId());

        return modelMapper.map(savedEmployee, EmployeeResponseDto.class);
    }


    // 4. Delete employee
    public String deleteEmployeeById(Integer empId) {
        log.info("Attempting to delete employee with ID: {}", empId);

        findEmployeeById(empId);
        employeeRepository.deleteById(empId);

        log.info("Employee with ID: {} deleted successfully", empId);
        return "Employee Deleted successfully";
    }


    // 5. PUT - Full update
    public EmployeeResponseDto updateEmployee(Integer empId, EmployeeDto employeeDto) {
        log.info("Updating employee (full) with ID: {}", empId);

        Employee existingEmployee = findEmployeeById(empId);
        modelMapper.map(employeeDto, existingEmployee);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        log.info("Employee ID: {} updated successfully (full)", empId);

        return modelMapper.map(updatedEmployee, EmployeeResponseDto.class);
    }


    // Helper for partial update
    private Employee partialUpdation(UpdateEmployeeDto updateEmployeeDto, Employee existingEmployee) {
        log.debug("Performing partial update for employee ID: {}", existingEmployee.getEmpId());

        if (updateEmployeeDto.getDesignation() != null) existingEmployee.setDesignation(updateEmployeeDto.getDesignation());
        if (updateEmployeeDto.getLocation() != null) existingEmployee.setLocation(updateEmployeeDto.getLocation());
        if (updateEmployeeDto.getEmpName() != null) existingEmployee.setEmpName(updateEmployeeDto.getEmpName());
        if (updateEmployeeDto.getSalary() != null) existingEmployee.setSalary(updateEmployeeDto.getSalary());

        return existingEmployee;
    }


    // 6. PATCH - Partial update
    public EmployeeResponseDto updatePartialEmployee(Integer empId, UpdateEmployeeDto updateEmployeeDto) {
        log.info("Updating employee (partial) with ID: {}", empId);

        Employee existingEmployee = findEmployeeById(empId);
        Employee partiallyUpdated = partialUpdation(updateEmployeeDto, existingEmployee);

        Employee savedEmployee = employeeRepository.save(partiallyUpdated);

        log.info("Employee ID: {} updated successfully (partial)", empId);

        return modelMapper.map(savedEmployee, EmployeeResponseDto.class);
    }

}
