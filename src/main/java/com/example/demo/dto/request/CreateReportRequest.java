package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateReportRequest {

    private String description;

    private UUID recipeId;
}