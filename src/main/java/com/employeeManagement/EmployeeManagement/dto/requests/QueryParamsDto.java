package com.employeeManagement.EmployeeManagement.dto.requests;

import com.employeeManagement.EmployeeManagement.enums.SortBy;
import com.employeeManagement.EmployeeManagement.enums.SortDir;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class QueryParamsDto {
    @Min(value = 0, message = "Page index must be greater than or equals to 0")
    private int pageNo = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size can not be greater than 100")
    private int pageSize = 10;


    private String location;
    private String designation;

    private SortBy sortBy;
    private SortDir sortDir;
}
