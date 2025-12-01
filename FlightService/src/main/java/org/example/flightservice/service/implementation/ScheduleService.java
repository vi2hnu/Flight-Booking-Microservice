package org.example.flightservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.flightservice.dto.ScheduleDTO;
import org.example.flightservice.dto.SeatsDTO;
import org.example.flightservice.exception.ScheduleNotFoundException;
import org.example.flightservice.model.entity.BookedSeats;
import org.example.flightservice.model.entity.Schedule;
import org.example.flightservice.repository.BookedSeatsRepository;
import org.example.flightservice.repository.ScheduleRepository;
import org.example.flightservice.service.ScheduleInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class ScheduleService implements ScheduleInterface {

    private final ScheduleRepository scheduleRepository;
    private final BookedSeatsRepository bookedSeatsRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,BookedSeatsRepository bookedSeatsRepository) {
        this.scheduleRepository = scheduleRepository;
        this.bookedSeatsRepository = bookedSeatsRepository;
    }

    @Override
    public ScheduleDTO getSchedule(Long scheduleId) {
       Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
       if(schedule == null){
           log.error("Schedule not found: {}",scheduleId);
           throw new ScheduleNotFoundException("Schedule not found: "+scheduleId);
       }
       log.info(new ScheduleDTO(schedule).toString());
       return new ScheduleDTO(schedule);
    }

    @Override
    public boolean checkSeats(Long scheduleId, SeatsDTO seatsDTO) {
        return seatsDTO.seats().stream()
                .anyMatch(seat-> bookedSeatsRepository.existsBySchedule_IdAndSeatPos(scheduleId,seat));
    }

    @Override
    public boolean reserveSeats(Long scheduleId, SeatsDTO seatsDTO) {
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);

        if(schedule == null){
            log.error("Schedule not found: {}",scheduleId);
            throw new ScheduleNotFoundException("Schedule not found: "+scheduleId);
        }

        seatsDTO.seats().stream()
                .forEach(seat ->bookedSeatsRepository.save(new BookedSeats(schedule,seat)));

        return true;
    }

    @Transactional
    @Override
    public void deleteSeats(Long scheduleId, SeatsDTO seatsDTO) {
        seatsDTO.seats().stream()
                .forEach(seat -> bookedSeatsRepository.deleteBySchedule_IdAndSeatPos(scheduleId,seat));

        addSeats(scheduleId,seatsDTO.seats().size());
    }

    @Override
    public void addSeats(Long scheduleId, int seats){
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);

        if(schedule == null){
           log.error("Schedule not found: {}",scheduleId);
           throw new ScheduleNotFoundException("Schedule not found: "+scheduleId);
        }

        schedule.setSeatsAvailable(seats+schedule.getSeatsAvailable());
        scheduleRepository.save(schedule);
    }
}
