package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class GradeRequest {

    private String grade;

    private UUID patientId;

    private UUID doctorOrClinicId;
}
