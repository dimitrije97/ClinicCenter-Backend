package com.example.demo.entity;

import com.example.demo.util.enums.ReasonOfUnavailability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule extends BaseEntity {

    private Date date;

    private LocalTime startAt;

    private LocalTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Examination examination;

    @Enumerated(EnumType.STRING)
    private ReasonOfUnavailability reasonOfUnavailability;

    private boolean approved;
}
