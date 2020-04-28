package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateReportRequest {

    private UUID reportId;

    private UUID doctorId;

    private String description;
}
