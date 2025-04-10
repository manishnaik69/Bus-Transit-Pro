package com.busmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for Bus Management System
 */
@SpringBootApplication
@EnableJpaRepositories
public class BusManagementApplication {
    
    /**
     * Main method to start the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BusManagementApplication.class, args);
    }
}