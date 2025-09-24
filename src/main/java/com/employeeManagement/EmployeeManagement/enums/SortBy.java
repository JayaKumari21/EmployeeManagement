package com.employeeManagement.EmployeeManagement.enums;

import java.lang.reflect.Field;

public enum SortBy {
    EMP_NAME("empName"),
    EMP_ID("empId"),
    SALARY("salary"),
    LOCATION("location"),
    DESIGNATION("designation");

    private final String field;

    //can use lombok annotation instead
    SortBy(String field) {
        this.field = field;
    }

    //can use lombok annotation instead
    public String getFieldName() {
        return field;
    }


}
