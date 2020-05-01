package com.example.demo.dto.request;

import lombok.Data;

@Data
public class UpdateClinicRequest {

    private String name;

    private String address;

    private String description;

    private String lat;

    private String lon;
}
