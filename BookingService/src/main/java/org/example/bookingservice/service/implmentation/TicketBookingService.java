package org.example.bookingservice.service.implmentation;

import lombok.extern.slf4j.Slf4j;
import org.example.bookingservice.dto.PassengerDTO;
import org.example.bookingservice.dto.ScheduleDTO;
import org.example.bookingservice.dto.SeatsDTO;
import org.example.bookingservice.dto.TicketBookingDTO;
import org.example.bookingservice.exception.InvalidScheduleTimeException;
import org.example.bookingservice.exception.SeatNotAvailableException;
import org.example.bookingservice.feign.FlightClient;
import org.example.bookingservice.model.entity.*;
import org.example.bookingservice.model.enums.Status;
import org.example.bookingservice.repository.TicketRepository;
import org.example.bookingservice.service.TicketBookingInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TicketBookingService implements TicketBookingInterface {

    private final TicketRepository ticketRepository;
    private final FlightClient flightClient;

    public TicketBookingService(TicketRepository ticketRepository, FlightClient flightClient) {
        this.ticketRepository = ticketRepository;
        this.flightClient = flightClient;
    }

    @Override
    public Ticket getTicket(TicketBookingDTO ticketDTO) {
        ScheduleDTO outbound = flightClient.getSchedule(ticketDTO.scheduleId());

        if (outbound == null) {
            log.error("Invalid schedule: {}",ticketDTO.scheduleId());
            throw new InvalidScheduleTimeException("Invalid schedule");
        }

        ScheduleDTO returnTrip = null;
        if (ticketDTO.returnTripId() != null) {
            returnTrip = flightClient.getSchedule(ticketDTO.returnTripId());
        }

        List<String> seats = ticketDTO.passengers().stream()
                .map(PassengerDTO::seatPos)
                .toList();
        SeatsDTO seatsDTO = new SeatsDTO(seats);
        boolean seatConflict = flightClient.checkSeats(outbound.id(), seatsDTO);

        if (seatConflict) {
            log.error("Seat already booked");
            throw new SeatNotAvailableException("Seat already booked");
        }

        if (outbound.seatsAvailable() < ticketDTO.passengers().size()) {
            log.error("Not enough seats available");
            throw new SeatNotAvailableException("Not enough seats available");
        }

        flightClient.reserveSeats(outbound.id(), seatsDTO);

        Ticket saved = createTicket(ticketDTO, outbound, returnTrip);

        savePassengers(saved, ticketDTO);

        return saved;
    }

    private Ticket createTicket(TicketBookingDTO dto, ScheduleDTO outbound, ScheduleDTO returnTrip) {

        Ticket ticket = new Ticket();
        String pnr = "PNR" + System.currentTimeMillis() + dto.user().getId();

        ticket.setPnr(pnr);
        ticket.setBookedByUsers(dto.user());

        ticket.setScheduleId(outbound.id());

        if (returnTrip != null) {
            ticket.setReturnTripScheduleId(returnTrip.id());
        }

        ticket.setStatus(Status.BOOKED);

        return ticketRepository.save(ticket);
    }

    private void savePassengers(Ticket ticket, TicketBookingDTO dto) {
        List<Passenger> passengers = new ArrayList<>();

        dto.passengers().forEach(p -> {
            Passenger passenger = new Passenger();
            passenger.setName(p.name());
            passenger.setGender(p.gender());
            passenger.setMealOption(p.meal());
            passenger.setSeatPosition(p.seatPos());
            passenger.setTicket(ticket);
            passengers.add(passenger);
        });

        ticket.setPassengers(passengers);
        ticketRepository.save(ticket);
    }
}
