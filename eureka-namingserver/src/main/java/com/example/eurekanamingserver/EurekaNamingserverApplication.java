package com.example.eurekanamingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaNamingserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaNamingserverApplication.class, args);
    }

}
