package com.example.order.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate employmentDate;
    private Double salary;
}
