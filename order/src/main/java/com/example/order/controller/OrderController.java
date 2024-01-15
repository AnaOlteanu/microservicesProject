package com.example.order.controller;

import com.example.order.model.Employee;
import com.example.order.model.Order;
import com.example.order.service.OrderService;
import com.example.order.service.client.EmployeeServiceProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final EmployeeServiceProxy employeeServiceProxy;

    public OrderController(OrderService orderService, EmployeeServiceProxy employeeServiceProxy) {
        this.orderService = orderService;
        this.employeeServiceProxy = employeeServiceProxy;
    }

    @Operation(summary = "List all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found with success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))})})
    @GetMapping(value = "/order/list", produces = {"application/hal+json"})
    public CollectionModel<Order> listAll() {
        List<Order> orderList = orderService.findAll();
        for (Order order: orderList) {
            Link selfLink = linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel();
            order.add(selfLink);
            Link employeeDetailsLink = linkTo(methodOn(OrderController.class).getOrderWithEmployee(order.getId())).withRel("orderWithEmployeeDetails");
            order.add(employeeDetailsLink);
        }

        Link link = linkTo(methodOn(OrderController.class).listAll()).withSelfRel();
        return CollectionModel.of(orderList, link);
    }

    @Operation(summary = "Find order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found with success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content)})
    @GetMapping("/order/{id}")
    public RepresentationModel<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.findById(id);
        Link employeeDetailsLink = linkTo(methodOn(OrderController.class).getOrderWithEmployee(id)).withRel("orderWithEmployeeDetails");
        order.add(employeeDetailsLink);
        return order;
    }

    @Operation(summary = "Find employee's details for one order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found with success!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found!",
                    content = @Content)})
    @GetMapping(value = "/order/employeeDetails/{id}")
    @CircuitBreaker(name="orderWithEmployee", fallbackMethod = "getOrderWithEmployeeFallback")
    public ResponseEntity<String> getOrderWithEmployee(@PathVariable Long id){
        Order order = orderService.findById(id);
        Employee employee = employeeServiceProxy.getEmployee(order.getEmployeeId());

        String firstName = "First Name: " + employee.getFirstName() + "\n";
        String lastName = "Last Name: " + employee.getLastName() + "\n";
        String employmentDate = "Employment Date: " + employee.getEmploymentDate() + "\n";
        String salary = "Salary with bonus: " + employee.getSalary() + "\n";
        String response = "For order with id: " + id + "\nEmployee details: \n" + firstName + lastName + employmentDate + salary;

        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<String> getOrderWithEmployeeFallback(Long id, Throwable throwable) {
        Order order = orderService.findById(id);
        String response = "For order with id: " + id + "\nNo employee's details found! \n";

        return ResponseEntity.ok().body(response);
    }
}
