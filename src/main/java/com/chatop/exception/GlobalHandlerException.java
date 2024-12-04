package com.chatop.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.JwtException;

/**
 * Global exception handler for managing various types of exceptions across the application.
 * This ensures consistent error messages and formats.
 */
@ControllerAdvice
public class GlobalHandlerException {

  /**
   * Handles database access errors.
   */
  @ExceptionHandler(DataAccessException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, Object>> handleDatabaseExceptions(
    DataAccessException ex
  ) {
    return buildErrorResponse(
      "500 - DATABASE_ERROR",
      "An error occurred while accessing the database. Please try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * Handles illegal argument errors.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentExceptions(
    IllegalArgumentException ex
  ) {
    return buildErrorResponse(
      "400 - INVALID_ARGUMENT",
      ex.getMessage(),
      HttpStatus.BAD_REQUEST
    );
  }

  /**
   * Handles invalid input exceptions.
   */
  @ExceptionHandler(InvalidInputException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Map<String, Object>> handleInvalidInputException(
    InvalidInputException ex
  ) {
    return buildErrorResponse(
      "401 - UNAUTHORIZED",
      ex.getMessage(),
      HttpStatus.UNAUTHORIZED
    );
  }

  /**
   * Handles validation errors for DTOs.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(
    MethodArgumentNotValidException ex
  ) {
    Map<String, String> fieldErrors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.put(error.getField(), error.getDefaultMessage());
    }

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("code", "400 - VALIDATION_ERROR");
    errorResponse.put("message", "Validation failed for the provided data.");
    errorResponse.put("details", fieldErrors);
    errorResponse.put("timestamp", System.currentTimeMillis());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles authentication errors.
   */
  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Map<String, Object>> handleAuthenticationExceptions(
    AuthenticationCredentialsNotFoundException ex
  ) {
    return buildErrorResponse(
      "401 - UNAUTHORIZED",
      "Authentication is required to access this resource. Please log in.",
      HttpStatus.UNAUTHORIZED
    );
  }

  /**
   * Handles JWT-specific errors, such as token expiration or invalid signature.
   */
  @ExceptionHandler(JwtException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Map<String, Object>> handleJwtException(
    JwtException ex
  ) {
    return buildErrorResponse(
      "401 - UNAUTHORIZED",
      "Invalid or expired token. Please log in again.",
      HttpStatus.UNAUTHORIZED
    );
  }

  /**
   * Handles access denial errors.
   */
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<Map<String, Object>> handleAccessDeniedExceptions(
    AccessDeniedException ex
  ) {
    return buildErrorResponse(
      "403 - ACCESS_DENIED",
      "You do not have the necessary permissions to access this resource.",
      HttpStatus.FORBIDDEN
    );
  }

  /**
   * Handles resource not found errors.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
    ResourceNotFoundException ex
  ) {
    return buildErrorResponse(
      "404 - RESOURCE_NOT_FOUND",
      ex.getMessage(),
      HttpStatus.NOT_FOUND
    );
  }

  /**
   * Handles general exceptions.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, Object>> handleGeneralExceptions(
    Exception ex
  ) {
    return buildErrorResponse(
      "500 - GENERAL_ERROR",
      "An unexpected error occurred. Please contact support.",
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * Utility method to build a standardized error response.
   */
  private ResponseEntity<Map<String, Object>> buildErrorResponse(
    String code,
    String message,
    HttpStatus httpStatus
  ) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("code", code);
    errorResponse.put("message", message);
    //errorResponse.put("timestamp", System.currentTimeMillis());
    return ResponseEntity.status(httpStatus).body(errorResponse);
  }
}
