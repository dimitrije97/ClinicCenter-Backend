package com.example.demo.dto.request;

import lombok.Data;

@Data
public class SearchCertifiedRecipesRequest {

    private String medicineName;

    private String diagnosisName;
}
