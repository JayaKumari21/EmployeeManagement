package com.employeeManagement.EmployeeManagement.services.impl;

import com.employeeManagement.EmployeeManagement.dto.EmployeeDto;
import com.employeeManagement.EmployeeManagement.exceptions.DuplicateEmployeeException;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import com.employeeManagement.EmployeeManagement.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl {
    //todo : constructor injection
//    @Autowired
//    private EmployeeRepository employeeRepository;

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

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
    public List<EmployeeDto> getAllEmployee() {
        List<Employee> list = employeeRepository.findAll();
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

    //-------------------------------------------------------------------------------------
    //5 Sort by id asc/desc
    public List<EmployeeDto> fetchSortedEmployeesById(String sortDir) {
        List<Employee> employeeList = new ArrayList<>();
        if (sortDir.equalsIgnoreCase("asc")) {
            employeeList = employeeRepository.findAllByOrderByEmpIdAsc();

        } else {
            employeeList = employeeRepository.findAllByOrderByEmpIdDesc();

        }
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee e : employeeList) {
            employeeDtoList.add(modelMapper.map(e, EmployeeDto.class));
        }
        return employeeDtoList;
    }

    //6 sort by any field
    //create Sort obj first
    public List<EmployeeDto> fetchSortedEmployeesByAnyField(String sortBy, String sortDir) {
        // validate the sorting field
        List<String> allowedFields = List.of("empId", "empName", "salary");
        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field for sorting :" + sortBy);
        }
        // decide the sorting direction
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDir.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        // create Sort object
        Sort sort = Sort.by(direction, sortBy);
        List<Employee> employeeList = employeeRepository.findAll(sort);
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee e : employeeList) {
            employeeDtoList.add(modelMapper.map(e, EmployeeDto.class));
        }
        return employeeDtoList;
    }

}

