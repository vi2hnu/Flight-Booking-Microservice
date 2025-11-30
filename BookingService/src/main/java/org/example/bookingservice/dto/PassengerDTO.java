package org.example.bookingservice.dto;

import jakarta.validation.constraints.NotBlank;
import org.example.bookingservice.model.enums.Gender;
import org.example.bookingservice.model.enums.Meal;

public record PassengerDTO(
        @NotBlank
        String name,

        @NotBlank
        Gender gender,

        @NotBlank
        Meal meal,

        @NotBlank
        String seatPos) {
}
