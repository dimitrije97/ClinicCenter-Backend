package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
public class CreateOperationRequest {

    private UUID patientId;

    private UUID doctorId;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date date;

    private LocalTime startAt;

    private LocalTime currentTime;
}
