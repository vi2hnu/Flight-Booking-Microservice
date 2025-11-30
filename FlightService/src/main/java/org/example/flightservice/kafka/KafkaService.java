package org.example.flightservice.kafka;

import org.example.flightservice.dto.AddSeatsDTO;
import org.example.flightservice.dto.KafkaSeatsDTO;
import org.example.flightservice.service.ScheduleInterface;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {

    private final ScheduleInterface scheduleInterface;

    public KafkaService(ScheduleInterface scheduleInterface){
        this.scheduleInterface = scheduleInterface;
    }

    @KafkaListener(topics = "ticket.booked")
    public void reserveSeats(KafkaSeatsDTO kafkaSeatsDTO){
        scheduleInterface.reserveSeats(kafkaSeatsDTO.scheduleId(),kafkaSeatsDTO.seats());
    }

    @KafkaListener(topics = "ticket.cancelled")
    public void deleteSeats(KafkaSeatsDTO kafkaSeatsDTO){
        scheduleInterface.deleteSeats(kafkaSeatsDTO.scheduleId(),kafkaSeatsDTO.seats());
    }

    @KafkaListener(topics = "add.seats")
    public void addSeats(AddSeatsDTO addSeatsDTO){
        scheduleInterface.addSeats(addSeatsDTO.scheduleId(),addSeatsDTO.numberOfSeats());
    }
}
