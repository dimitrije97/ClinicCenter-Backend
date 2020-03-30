package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
public class AvailableDoctorsRequest {

    private LocalTime startAt;

    private UUID examinationTypeId;

    private UUID clinicId;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date date;
}
