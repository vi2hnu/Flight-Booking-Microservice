package org.example.bookingservice.model.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.example.bookingservice.model.enums.Status;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pnr;

    @ManyToOne
    private Users bookedByUsers;

    private Long scheduleId;

    private Long returnTripScheduleId;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Passenger> passengers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

}
