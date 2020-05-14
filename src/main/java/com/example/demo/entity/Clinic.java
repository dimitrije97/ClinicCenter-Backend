package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Clinic extends BaseEntity {

    private String name;

    private String address;

    private String description;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Admin> admins = new ArrayList<>();

    private boolean deleted;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor> doctors = new ArrayList<>();

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nurse> nurses = new ArrayList<>();

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EmergencyRoom> emergencyRooms = new ArrayList<>();

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();

    private String lat;

    private String lon;

    //ako je neka klinika izmenjena, dodata ili obrisana u momentu kada je pacijent prikazao klinike, pacijent ce dobiti nevalidne podatke
    //vise adminitratora ne sme da izmeni istu kliniku u isto vreme
    @Version
    @Column(name="version",columnDefinition = "integer DEFAULT 0",nullable = false)
    private int version;
}
