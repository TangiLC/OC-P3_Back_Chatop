package com.chatop.dto;

import java.util.List;

/**
 * Wrapper class for encapsulating a list of rentals in a response object.
 */
public class RentalsResponseDTO {

    private List<RentalDTO> rentals;

    public RentalsResponseDTO(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }

    public List<RentalDTO> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }
}
