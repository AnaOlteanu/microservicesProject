package com.example.order.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String details;

    private String country;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "order_date")
    private LocalDate orderDate;
}
