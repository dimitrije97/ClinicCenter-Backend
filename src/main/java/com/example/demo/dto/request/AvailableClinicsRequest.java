package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
public class AvailableClinicsRequest {

    private UUID examinationTypeId;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date date;
}
