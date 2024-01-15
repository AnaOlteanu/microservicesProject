package com.example.employee.service.client;

import com.example.employee.model.Bonus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "bonus")
public interface BonusServiceProxy {
    @GetMapping(value = "/bonus", consumes = "application/json")
    public Bonus getBonus();
}
