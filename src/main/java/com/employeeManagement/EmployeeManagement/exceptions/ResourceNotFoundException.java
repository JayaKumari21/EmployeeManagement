package com.employeeManagement.EmployeeManagement.exceptions;


public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
