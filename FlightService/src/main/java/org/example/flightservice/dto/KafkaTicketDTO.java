package org.example.flightservice.dto;

import org.example.flightservice.model.entity.Users;

import java.util.List;

public record KafkaTicketDTO(
        Users user,
        ScheduleDTO scheduleDTO,
        ScheduleDTO returnTripDTO,
        List<PassengerDTO> passengers
){}
