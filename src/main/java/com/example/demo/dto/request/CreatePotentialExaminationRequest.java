package com.example.demo.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
public class CreatePotentialExaminationRequest {

    private Date date;

    private LocalTime startAt;

    private UUID doctorId;

    private UUID emergencyRoomId;
}
