package com.chatop.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for transferring user data.
 */
public class UserDTO {

    @NotNull(message = "User ID cannot be null")
    private Integer id;

    @NotBlank(message = "User name is required")
    private String name;

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructs a UserDTO with all fields.
     *
     * @param id The user ID.
     * @param name The user's name.
     * @param email The user's email.
     * @param createdAt The creation timestamp.
     * @param updatedAt The last updated timestamp.
     */
    public UserDTO(Integer id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The User entity.
     * @return The corresponding UserDTO.
     */
    public static UserDTO fromEntity(com.chatop.model.User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
