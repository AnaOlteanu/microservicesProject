package com.example.order.service.client;


import com.example.order.model.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "employee")
public interface EmployeeServiceProxy {
    @GetMapping(value = "/employee/list", consumes = "application/json")
    CollectionModel<Employee> listAll();

    @GetMapping(value = "/employee/{id}", consumes = "application/json")
    Employee getEmployee(@PathVariable Long id) ;
}
