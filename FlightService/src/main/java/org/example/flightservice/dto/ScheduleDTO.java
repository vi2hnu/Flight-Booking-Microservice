package org.example.flightservice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleDTO(
        @NotNull
        Long flightId,

        @NotNull
        Long fromCityId,

        @NotNull
        Long toCityId,

        @NotNull
        LocalDate departureDate,

        @NotNull
        LocalDateTime departureTime,

        @NotNull
        Float price,

        @NotNull
        Integer duration
) {}

