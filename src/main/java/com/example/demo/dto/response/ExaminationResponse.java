package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationResponse {

    private UUID id;

    private Date date;

    private LocalTime startAt;

    private LocalTime endAt;

    private String doctorFirstName;

    private String doctorLastName;

    private String emergencyRoomName;

    private String patientFirstName;

    private String patientLastName;

    private String examinationTypeName;

    private String clinicName;
}
