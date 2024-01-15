package com.example.bonus.controller;

import com.example.bonus.config.PropertiesConfig;
import com.example.bonus.model.Bonus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final PropertiesConfig configuration;

    public Controller(PropertiesConfig configuration) {
        this.configuration = configuration;
    }

    @GetMapping("/bonus")
    public Bonus getBonus() {
        return new Bonus(configuration.getMonth(), configuration.getYear(), configuration.getVersion());
    }
}
