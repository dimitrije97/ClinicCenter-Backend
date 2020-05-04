package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateRecipeRequest {

    private UUID medicineId;

    private UUID diagnosisId;

    private UUID clinicId;
}
