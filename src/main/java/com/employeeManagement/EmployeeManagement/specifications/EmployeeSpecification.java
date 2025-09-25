package com.employeeManagement.EmployeeManagement.specifications;

import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.management.Query;

public class EmployeeSpecification {

    public static Specification<Employee> hasField(String fieldName, String fieldValue) {
        return (root, query, cb) ->
                fieldValue == null || fieldValue.isBlank()
                        ? null
                        : cb.equal(root.get(fieldName), fieldValue);
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
