package com.chatop.dto;

/**
 * DTO for sending a response containing only the message content.
 */
public class MessageResponseDTO {

    /** The content of the message. */
    private String message;

    /**
     * Constructs a MessageResponseDTO.
     *
     * @param message The content of the message.
     */
    public MessageResponseDTO(String message) {
        this.message = message;
    }

    /**
     * Gets the message content.
     *
     * @return The content of the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content.
     *
     * @param message The new message content.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
