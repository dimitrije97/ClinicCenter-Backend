package com.example.demo.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateReportRequest {

    private String description;

    private UUID recipeId;

    private UUID examinationId;

    private LocalTime currentTime;

    private UUID doctorId;
}
