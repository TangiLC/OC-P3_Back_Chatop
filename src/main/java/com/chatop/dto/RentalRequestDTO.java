package com.chatop.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for handling rental creation and update requests.
 */
public class RentalRequestDTO {

  @NotBlank(message = "Rental name is required")
  private String name;

  @NotNull(message = "Rental surface is required")
  @Min(value = 1, message = "Rental surface must be greater than 0")
  private Integer surface;

  @NotNull(message = "Rental price is required")
  @Min(value = 1, message = "Rental price must be greater than 0")
  private Integer price;

  //@NotBlank(message = "Rental picture URL is required")
  private MultipartFile picture;

  private String description;

  /*@NotNull(message = "Owner ID is required")
  private Integer ownerId;*/

  //  Getters and Setters

  public RentalRequestDTO(
    String name,
    Integer surface,
    Integer price,
    MultipartFile picture,
    String description
  ) {
    this.name = name;
    this.surface = surface;
    this.price = price;
    this.picture = picture;
    this.description = description;
  }

  // Getters and Setters

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

  public MultipartFile getPicture() {
    return picture;
  }

  public void setPicture(MultipartFile picture) {
    this.picture = picture;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return (
      "RentalRequestDTO{" +
      "name='" +
      name +
      '\'' +
      ", surface=" +
      surface +
      ", price=" +
      price +
      ", picture=" +
      (picture != null ? picture.getOriginalFilename() : "null") +
      ", description='" +
      description +
      '\'' +
      '}'
    );
  }
}
