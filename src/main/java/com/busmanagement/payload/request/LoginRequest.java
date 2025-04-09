package com.busmanagement.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * Represents a login request payload
 */
public class LoginRequest {
    
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}