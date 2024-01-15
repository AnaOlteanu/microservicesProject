package com.example.employee.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Bonus {
    private int month;
    private int year;
    private String version;
}
