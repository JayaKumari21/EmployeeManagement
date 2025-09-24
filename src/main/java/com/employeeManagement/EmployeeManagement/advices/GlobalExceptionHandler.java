package com.employeeManagement.EmployeeManagement.advices;

import com.employeeManagement.EmployeeManagement.exceptions.DuplicateEmployeeException;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<?>> buildApiResponse(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse<>(apiError), apiError.getHttpStatusCode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .errorMessage(ex.getMessage())
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .build();

        return buildApiResponse(apiError);
    }

    @ExceptionHandler(DuplicateEmployeeException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateEmployeeException(DuplicateEmployeeException ex) {
        ApiError apiError = ApiError.builder()
                .errorMessage(ex.getMessage())
                .httpStatusCode(HttpStatus.CONFLICT)
                .build();
        return buildApiResponse(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> subErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        System.out.println(ex.getMessage());
        ApiError apiError = ApiError.builder()
                .errorMessage("Input validation failed")
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .subError(subErrors)
                .build();
        return buildApiResponse(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = ApiError.builder()
                .errorMessage("Input validation failed")
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .subError(Collections.singletonList(ex.getMessage()))
                .build();
        return buildApiResponse(apiError);
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {

        ApiError apiError = ApiError.builder()
                .errorMessage(ex.getMessage())
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return buildApiResponse(apiError);
    }


}
