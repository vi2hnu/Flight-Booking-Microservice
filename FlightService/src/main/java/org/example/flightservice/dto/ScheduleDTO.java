package org.example.flightservice.dto;

import jakarta.validation.constraints.NotNull;
import org.example.flightservice.model.entity.Schedule;

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
) {
    public ScheduleDTO(Schedule s){
        this(
                s.getFlight() != null ? s.getFlight().getId() : null,
                s.getFromCity() != null ? s.getFromCity().getId() : null,
                s.getToCity() != null ? s.getToCity().getId() : null,
                s.getDepartureDate(),
                s.getDepartureTime(),
                s.getPrice(),
                s.getDuration()
        );
    }

}

