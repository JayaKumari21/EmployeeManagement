package com.employeeManagement.EmployeeManagement.specifications;

import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.management.Query;
import java.time.LocalDateTime;

public class EmployeeSpecification {

    //Case 1 : equals/exact match
    public static Specification<Employee> hasField(String fieldName, String fieldValue) {
        return (root, query, cb) ->
                fieldValue == null || fieldValue.isBlank()
                        ? null
                        : cb.equal(root.get(fieldName), fieldValue);
    }

    // Case 2 : contains
    public static Specification<Employee> containsPattern(String fieldName, String fieldValue) {
        return (root, query, cb) ->
                fieldValue == null || fieldValue.isBlank()
                        ? null
                        : cb.like(root.get(fieldName), "%" + fieldValue + "%");
    }

    // Case 3 : greater>
    public static Specification<Employee> greaterThanNum(String fieldName, Integer fieldValue) {
        return (root, query, cb) ->
                fieldValue == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get(fieldName), fieldValue);
    }

    // Case 4 : between
    public static Specification<Employee> valueBetween(String fieldName, LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) ->
                start == null || end == null
                        ? null
                        : cb.between(root.get(fieldName), start, end);
    }
    /*
    public static Specification<Employee> hasLocation(String location) {
        return (root, query, criteriaBuilder) ->
                location == null || location.isBlank()
                        ? null
                        : criteriaBuilder.equal(root.get("location"), location);
    }

    public static Specification<Employee> hasDesignation(String designation) {
        return (root, query, cb) ->
                designation == null || designation.isBlank()
                        ? null
                        : cb.equal(root.get("designation"), designation);


    }
*/
}
