package com.employeeManagement.EmployeeManagement.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Data
@Builder
public class ApiError {

    private String errorMessage;
    private HttpStatusCode httpStatusCode;
    private List<String> subError;


}
