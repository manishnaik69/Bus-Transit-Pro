package com.busmanagement.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Singleton implementation for database connection.
 * This class ensures that only one connection is created to the database,
 * following the Singleton design pattern.
 */
@Component
public class DatabaseConnection {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String dbUsername;
    
    @Value("${spring.datasource.password}")
    private String dbPassword;
    
    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets the singleton instance of the DatabaseConnection.
     * 
     * @return The singleton instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Gets a connection to the database.
     * 
     * @return A Connection object representing a connection to the database
     */
    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                logger.info("Database connection established");
            } catch (SQLException e) {
                logger.error("Error connecting to database: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to connect to database", e);
            }
        }
        return connection;
    }
    
    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection: {}", e.getMessage(), e);
            }
        }
    }
}
