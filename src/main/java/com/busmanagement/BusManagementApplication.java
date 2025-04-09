package com.busmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main class to start the Bus Management System application
 */
@SpringBootApplication
@EntityScan("com.busmanagement.model")
@EnableJpaRepositories("com.busmanagement.repository")
public class BusManagementApplication {

    /**
     * Main method that starts the Spring Boot application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BusManagementApplication.class, args);
    }
}