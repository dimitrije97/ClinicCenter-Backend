package com.example.demo.dto.request;

import lombok.Data;

@Data
public class UpdatePatientRequest {

    private String firstName;

    private String lastName;

    private String country;

    private String city;

    private String address;

    private String phone;
}
