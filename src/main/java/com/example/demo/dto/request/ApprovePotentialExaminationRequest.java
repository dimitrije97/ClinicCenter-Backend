package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ApprovePotentialExaminationRequest {

    private UUID examinationId;

    private UUID patientId;
}
