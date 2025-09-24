package com.employeeManagement.EmployeeManagement.advices;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private T data;
    private LocalDateTime timeStamp;
    private ApiError error;

    ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    ApiResponse(T data) {
        this();
        this.data = data;
    }

    ApiResponse(ApiError error) {
        this();
        this.error = error;
    }

}
