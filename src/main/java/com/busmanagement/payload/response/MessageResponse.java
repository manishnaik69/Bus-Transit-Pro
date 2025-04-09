package com.busmanagement.payload.response;

/**
 * Represents a simple message response payload
 */
public class MessageResponse {

    private String message;

    /**
     * Constructor for MessageResponse
     * 
     * @param message The message
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter and setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}