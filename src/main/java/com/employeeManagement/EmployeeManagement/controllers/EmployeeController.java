package com.employeeManagement.EmployeeManagement.controllers;

import com.employeeManagement.EmployeeManagement.dto.requests.EmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.requests.QueryParamsDto;
import com.employeeManagement.EmployeeManagement.dto.requests.UpdateEmployeeDto;
import com.employeeManagement.EmployeeManagement.dto.responses.EmployeeResponseDto;
import com.employeeManagement.EmployeeManagement.services.impl.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@Tag(name = "Employee API", description = "All CRUD APIs")
public class EmployeeController {

//    @Autowired
//    private EmployeeServiceImpl employeeService;

    // Constructor injection
    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    // 1 Add employee
    @Operation(summary = "To create employee")
    @PostMapping()
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeResponseDto savedEmployee = employeeService.addEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    //2 Get all employee
    //filter+MultiAttribute filter
   /* @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAll(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        System.out.println("Value of sortBy in controller : " + sortBy);
        List<EmployeeDto> employeeDtoList = employeeService.getAllEmployee(sortBy, sortDir, pageNo, pageSize);
        return new ResponseEntity<>(employeeDtoList, HttpStatus.OK);
    }*/

    @Operation(summary = "Get all the employee details")
    @GetMapping()
    public ResponseEntity<List<EmployeeResponseDto>> getAll(@Valid @ParameterObject @ModelAttribute QueryParamsDto queryParamsDto) throws Exception {
        List<EmployeeResponseDto> employeeResponseDto = employeeService.getAllEmployee(queryParamsDto);

        return new ResponseEntity<>(employeeResponseDto, HttpStatus.OK);
    }

    //3 Get Employee by id
    @Operation(summary = "Get employee by id")
    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Successfully retrieved employee",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = EmployeeResponseDto.class)
//                    )
//            ),
            @ApiResponse(responseCode = "200", description = "created employee ID supplied"),
            @ApiResponse(responseCode = "400", description = "Invalid employee ID supplied", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
    })

    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable int empId) {
        EmployeeResponseDto employeeResponseDto = employeeService.getEmployeeById(empId);
        return ResponseEntity.ok(employeeResponseDto);
    }

    //4 Delete Employee by id
    @Operation(summary = "Delete employee by id")
    @DeleteMapping("/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int empId) {
        return new ResponseEntity<>(employeeService.deleteEmployeeById(empId), HttpStatus.OK);
    }


    // 5 Put :- Full
    @Operation(summary = "Fully update an employee")
    @PutMapping("/{empId}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Integer empId, @Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(empId, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    // 6 Patch :- Partial
    @Operation(summary = "Partially update an employee")
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updatePartial(@PathVariable("id") Integer empId, @Valid @RequestBody UpdateEmployeeDto updateEmployeeDto) {
//        System.out.println("In controller " + updates.keySet());
        EmployeeResponseDto updatedEmployee = employeeService.updatePartialEmployee(empId, updateEmployeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }
}
