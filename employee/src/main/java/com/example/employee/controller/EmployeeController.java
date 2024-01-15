package com.example.employee.controller;

import com.example.employee.model.Bonus;
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import com.example.employee.service.client.BonusServiceProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BonusServiceProxy bonusServiceProxy;

    public EmployeeController(EmployeeService employeeService, BonusServiceProxy bonusServiceProxy) {
        this.employeeService = employeeService;
        this.bonusServiceProxy = bonusServiceProxy;
    }

    @Operation(summary = "List all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found with success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))})})
    @GetMapping(value = "/employee/list", produces = {"application/hal+json"})
    public CollectionModel<Employee> listAll() {
        List<Employee> employeeList = employeeService.findAll();
        for (Employee employee: employeeList) {
            Link selfLink = linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel();
            employee.add(selfLink);
            Link deleteLink = linkTo(methodOn(EmployeeController.class).deleteEmployee(employee.getId())).withRel("deleteEmployee");
            employee.add(deleteLink);
        }

        Link link = linkTo(methodOn(EmployeeController.class).listAll()).withSelfRel();
        return CollectionModel.of(employeeList, link);
    }

    @Operation(summary = "Find employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found with success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @GetMapping("/employee/{id}")
    @CircuitBreaker(name="bonusById", fallbackMethod = "getEmployeeFallback")
    public Employee getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        Bonus bonus = bonusServiceProxy.getBonus();

        Double newSalary = employee.getSalary() * (1 + (double)bonus.getMonth()/100);
        employee.setSalary(newSalary);

        log.info("Employee {} has salary {} with bonus {} ",employee.getFirstName(), newSalary, bonus.getMonth());
        return employee;
    }

    private Employee getEmployeeFallback(Long id, Throwable throwable){
        return employeeService.findById(id);
    }

    @Operation(summary = "Delete employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @DeleteMapping("/employee/{id}")
    public Employee deleteEmployee(@PathVariable Long id) {
        return employeeService.delete(id);
    }
}
