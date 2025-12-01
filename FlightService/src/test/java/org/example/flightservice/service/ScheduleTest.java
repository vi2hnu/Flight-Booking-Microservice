package org.example.flightservice.service;

import org.example.flightservice.dto.KafkaTicketDTO;
import org.example.flightservice.dto.PassengerDTO;
import org.example.flightservice.dto.ScheduleDTO;
import org.example.flightservice.dto.SeatsDTO;
import org.example.flightservice.exception.ScheduleNotFoundException;
import org.example.flightservice.model.entity.Schedule;
import org.example.flightservice.model.entity.Users;
import org.example.flightservice.model.enums.Gender;
import org.example.flightservice.model.enums.Meal;
import org.example.flightservice.repository.BookedSeatsRepository;
import org.example.flightservice.repository.ScheduleRepository;
import org.example.flightservice.service.implementation.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private BookedSeatsRepository bookedSeatsRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void scheduleTest_ThrowsScheduleNotFound(){
        when(scheduleRepository.findScheduleById(1L)).thenReturn(null);
        assertThrows(ScheduleNotFoundException.class,()->scheduleService.getSchedule(1L));
    }

    @Test
    void scheduleTest_ScheduleSuccess(){
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        when(scheduleRepository.findScheduleById(1L)).thenReturn(schedule);
        scheduleService.getSchedule(1L);
        verify(scheduleRepository,times(1)).findScheduleById(1L);
    }

    @Test
    void scheduleTest_ChecksSeats(){
        List<String> seats = new ArrayList<>();
        seats.add("1A");
        scheduleService.checkSeats(1L,new SeatsDTO(seats));
        verify(bookedSeatsRepository,times(1)).existsBySchedule_IdAndSeatPos(1L,"1A");
    }

    @Test
    void reserveSeats_ThrowsScheduleNotFound(){
        ScheduleDTO dto = new ScheduleDTO(1L,1L,1L,1L, LocalDate.now(),
                LocalDateTime.now(),500f,10,60);
        KafkaTicketDTO kafkaDto = new KafkaTicketDTO(new Users(),dto,null,List.of(), "PNR1");
        when(scheduleRepository.findScheduleById(1L)).thenReturn(null);
        assertThrows(ScheduleNotFoundException.class,()->scheduleService.reserveSeats(kafkaDto));
    }

    @Test
    void reserveSeats_Success(){
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        PassengerDTO p = new PassengerDTO("name", Gender.MALE, Meal.VEG, "1A");
        ScheduleDTO dto = new ScheduleDTO(schedule);
        KafkaTicketDTO kafkaDto = new KafkaTicketDTO(new Users(),dto,null,List.of(p),"PNR1");
        when(scheduleRepository.findScheduleById(1L)).thenReturn(schedule);
        scheduleService.reserveSeats(kafkaDto);
        verify(bookedSeatsRepository,times(1)).save(any());
    }

    @Test
    void deleteSeats_Success(){
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setSeatsAvailable(10);
        PassengerDTO p = new PassengerDTO("name", Gender.MALE, Meal.VEG, "1A");
        ScheduleDTO dto = new ScheduleDTO(schedule);
        KafkaTicketDTO kafkaDto = new KafkaTicketDTO(new Users(),dto,null,List.of(p),"PNR1");
        when(scheduleRepository.findScheduleById(1L)).thenReturn(schedule);
        scheduleService.deleteSeats(kafkaDto);
        verify(bookedSeatsRepository,times(1)).deleteBySchedule_IdAndSeatPos(1L,"1A");
    }

    @Test
    void addSeats_ThrowsScheduleNotFound(){
        when(scheduleRepository.findScheduleById(1L)).thenReturn(null);
        assertThrows(ScheduleNotFoundException.class,()->scheduleService.addSeats(1L,5));
    }

    @Test
    void addSeats_Success(){
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setSeatsAvailable(10);
        when(scheduleRepository.findScheduleById(1L)).thenReturn(schedule);
        scheduleService.addSeats(1L,5);
        verify(scheduleRepository,times(1)).save(schedule);
    }
}
