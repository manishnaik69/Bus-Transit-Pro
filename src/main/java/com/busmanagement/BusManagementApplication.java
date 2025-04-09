package com.busmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Bus Management System
 */
@SpringBootApplication
public class BusManagementApplication {

    /**
     * Main method to start the application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BusManagementApplication.class, args);
    }
}