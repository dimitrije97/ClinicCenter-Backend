package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicResponse {

    private String name;

    private String address;

    private String description;

    private UUID id;

    private String lat;

    private String lon;
}
