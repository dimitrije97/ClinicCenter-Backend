package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {

    private UUID id;

    private String medicineName;

    private String diagnosisName;

    private String nurseName;

    private String nurseSurname;
}
