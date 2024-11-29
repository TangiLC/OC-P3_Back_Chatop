package com.chatop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) for transferring rental data.
 */
public class RentalDTO {

  private Integer id;
  private String name;
  private Integer surface;
  private Integer price;
  private String picture;
  private String description;
  @JsonProperty("owner_id")
  private Integer ownerId;

  @JsonProperty("created_at")
  private LocalDateTime createdAt;

  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;

  /**
   * Constructs a RentalDTO with all fields.
   *
   * @param id The rental ID.
   * @param name The rental name.
   * @param surface The surface of the rental in square meters.
   * @param price The rental price.
   * @param picture The URL of the rental's picture.
   * @param description The description of the rental.
   * @param ownerId The owner's ID.
   * @param createdAt The creation timestamp of the rental.
   * @param updatedAt The last updated timestamp of the rental.
   */
  public RentalDTO(
    Integer id,
    String name,
    Integer surface,
    Integer price,
    String picture,
    String description,
    Integer ownerId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
  ) {
    this.id = id;
    this.name = name;
    this.surface = surface;
    this.price = price;
    this.picture = picture;
    this.description = description;
    this.ownerId = ownerId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  /**
   * Converts a Rental entity to a RentalDTO.
   *
   * @param rental The Rental entity.
   * @return The corresponding RentalDTO.
   */
  public static RentalDTO fromEntity(com.chatop.model.Rental rental) {
    if (rental == null) {
      throw new IllegalArgumentException("Rental entity cannot be null");
    }

    if (rental.getOwner() == null || rental.getOwner().getId() == null) {
      throw new IllegalArgumentException(
        "Rental owner or owner ID cannot be null"
      );
    }
    return new RentalDTO(
      rental.getId(),
      rental.getName(),
      rental.getSurface(),
      rental.getPrice(),
      rental.getPicture(),
      rental.getDescription(),
      rental.getOwner().getId(),
      rental.getCreatedAt(),
      rental.getUpdatedAt()
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

  public Integer getSurface() {
    return surface;
  }

  public void setSurface(Integer surface) {
    this.surface = surface;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Integer ownerId) {
    this.ownerId = ownerId;
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
