package com.example.employee.service;

import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee findById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if(employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found!");
        }

        return employeeOptional.get();
    }

    @Override
    public List<Employee> findBySalaryGreaterThan(double salary) {
        return employeeRepository.findBySalaryGreaterThan(salary);
    }

    @Override
    public Employee findByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Employee> employeeOptional = employeeRepository.findByFirstNameAndLastName(firstName, lastName);

        if(employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with first name:  " + firstName + " and last name: " + lastName + " not found!");
        }

        return employeeOptional.get();
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee delete(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if(employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found!");
        }

        employeeRepository.deleteById(id);

        return employeeOptional.get();
    }
}
