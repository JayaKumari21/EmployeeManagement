package com.employeeManagement.EmployeeManagement.repositories;

import com.employeeManagement.EmployeeManagement.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
JpaRepository requires two type parameters:
1. The Entity class
2. The Primary key type (usually Long, Integer, etc.)
 */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
}
