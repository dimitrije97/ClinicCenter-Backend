package com.example.demo.entity;

import com.example.demo.util.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Examination extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_room_id")
    private EmergencyRoom emergencyRoom;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private RequestType status;

    @OneToMany(mappedBy = "examination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    //vise pacijenata ne moze da zakaze isti predefinisani pregled
    //vise pacijenata ne moze da odobri pregled kojem je dodeljena sala od strane admina u isto vreme
    //dva administratora ne smeju dodeljivati salu za isti zahtev za pregled
    //dva administratora ne smeju dodeljivati istog lekara za operaciju  u isto vreme
    @Version
    @Column(name="version",columnDefinition = "integer DEFAULT 0",nullable = false)
    private int version;
}
