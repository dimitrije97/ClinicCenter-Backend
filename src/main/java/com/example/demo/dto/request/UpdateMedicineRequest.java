package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateMedicineRequest {

    private UUID id;

    private String name;
}
