package com.example.employee.service;

import com.example.employee.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee findById(Long id);
    List<Employee> findBySalaryGreaterThan(double salary);
    Employee findByFirstNameAndLastName(String firstName, String lastName);

    Employee save(Employee employee);
    List<Employee> findAll();
    Employee delete(Long id);

}
