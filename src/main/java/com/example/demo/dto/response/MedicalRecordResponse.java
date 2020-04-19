package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponse {

    private UUID id;

    private String height;

    private String weight;

    private String allergy;

    private String patientName;

    private String patientSurname;

    private String patientEmail;
}
