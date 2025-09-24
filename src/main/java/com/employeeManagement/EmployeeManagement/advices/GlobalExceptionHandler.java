package com.employeeManagement.EmployeeManagement.advices;

import com.employeeManagement.EmployeeManagement.exceptions.DuplicateEmployeeException;
import com.employeeManagement.EmployeeManagement.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
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

    //enum + validation failed

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> subErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    // Handle enum conversion errors
                    if (error.contains(TypeMismatchException.class)) {
                        Object rejectedValue = error.getRejectedValue();
                        Class<?> targetType = error.unwrap(TypeMismatchException.class).getRequiredType();
                        if (targetType != null && targetType.isEnum()) {
                            return error.getField() + ": Invalid value '" + rejectedValue +
                                    "'. Allowed values are: " + Arrays.toString(targetType.getEnumConstants());
                        }
                    }
                    return error.getField() + ": " + error.getDefaultMessage();
                })
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .errorMessage("Input validation failed")
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

    // Catch Jackson wrapper for unknown fields
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException upe = (UnrecognizedPropertyException) cause;
            String message = String.format("Unknown field '%s' in request", upe.getPropertyName());

            ApiError apiError = ApiError.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .errorMessage("Input validation failed")
                    .subError(List.of(message))
                    .build();
            return buildApiResponse(apiError);
        }

        // fallback for other parse errors
        ApiError apiError = ApiError.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .errorMessage("Malformed JSON request")
                .subError(List.of(ex.getMessage()))
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
