package com.employeeManagement.EmployeeManagement.dto.requests;

import com.employeeManagement.EmployeeManagement.enums.SortBy;
import com.employeeManagement.EmployeeManagement.enums.SortDir;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QueryParamsDto {

    @Schema(description = "Page index (0-based)", example = "0")
    @Min(value = 0, message = "Page index must be greater than or equals to 0")
    @NotNull()
    private Integer pageNo = 0;


    @Schema(description = "Page size (1-100)", example = "10")
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size can not be greater than 100")
    @NotNull
    private Integer pageSize = 10;


    private String location;
    private String designation;
    private String empName;
    private Integer salary;

    @Schema(description = "Start date-time for filtering and must select endtime too")
    private LocalDateTime startTime;

    @Schema(description = "End date-time for filtering and must select startTime too")
    private LocalDateTime endTime;

    private SortBy sortBy;
    private SortDir sortDir;
}
