package com.example.employee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "employee")
public class Employee extends RepresentationModel<Employee> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    @Size(max = 20, message = "Maximum 20 characters")
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 20, message = "Maximum 20 characters")
    private String lastName;

    @Column(name = "employment_date")
//    @JsonFormat(pattern = "DD-MM-YYYY")
    @Past
    private LocalDate employmentDate;

    @Min(value = 100, message = "Salary must be greater than 100")
    private Double salary;

}
