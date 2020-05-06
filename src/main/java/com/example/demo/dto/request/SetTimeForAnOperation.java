package com.example.demo.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class SetTimeForAnOperation {

    private UUID examinationId;

    private LocalTime startAt;
}
