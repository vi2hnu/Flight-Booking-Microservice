package org.example.flightservice.kafka;

import org.example.flightservice.dto.KafkaTicketDTO;
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
    public void reserveSeats(KafkaTicketDTO kafkaTicketDTO){
        scheduleInterface.reserveSeats(kafkaTicketDTO);
    }

    @KafkaListener(topics = "ticket.cancelled")
    public void deleteSeats(KafkaTicketDTO kafkaTicketDTO){
        scheduleInterface.deleteSeats(kafkaTicketDTO);
    }
}
