package com.employeeManagement.EmployeeManagement.enums;

import java.lang.reflect.Field;

public enum SortBy {
    EMP_NAME("empName"),
    EMP_ID("empId"),
    SALARY("salary"),
    LOCATION("location"),
    DESIGNATION("designation");

    private final String fieldName;

    //can use lombok annotation instead
    SortBy(String fieldName) {
        this.fieldName = fieldName;
    }

    //can use lombok annotation instead
    public String getFieldName() {
        return fieldName;
    }


}
