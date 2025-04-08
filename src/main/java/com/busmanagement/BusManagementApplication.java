package com.busmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Bus Management System application.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
@EnableScheduling
public class BusManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusManagementApplication.class, args);
    }
}
