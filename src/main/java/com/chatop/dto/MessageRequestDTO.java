package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for message creation requests.
 */
public class MessageRequestDTO {

  @NotNull(message = "User ID is required")
  @JsonProperty("user_id")
  private Integer userId;

  @NotNull(message = "Rental ID is required")
  @JsonProperty("rental_id")
  private Integer rentalId;

  @NotBlank(message = "Message content cannot be empty")
  private String message;

  // Getters and setters
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getRentalId() {
    return rentalId;
  }

  public void setRentalId(Integer rentalId) {
    this.rentalId = rentalId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
