package com.chatop.dto;

/**
 * DTO for login response containing the JWT token.
 */
public class LoginResponseDTO {

  private String token;

  public LoginResponseDTO(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
