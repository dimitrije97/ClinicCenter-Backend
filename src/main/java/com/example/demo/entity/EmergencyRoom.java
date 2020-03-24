package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyRoom extends BaseEntity {

    @Column(unique = true)
    private String number;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    private boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emergencyRoom")
    private Set<Examination> examinations = new HashSet<>();
}
