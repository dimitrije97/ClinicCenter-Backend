package com.example.demo.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class UpdateMedicalRecordRequest {

    private String height;

    private String weight;

    private String allergy;

    private String diopter;

    private String patientEmail;

    private UUID doctorId;

    private LocalTime currentTime;
}
