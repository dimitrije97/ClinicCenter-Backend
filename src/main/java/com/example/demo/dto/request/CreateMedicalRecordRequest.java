package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateMedicalRecordRequest {

    private UUID patientId;

    private String height;

    private String weight;

    private String allergy;
}
