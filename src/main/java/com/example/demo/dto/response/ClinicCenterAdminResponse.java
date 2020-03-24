package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicCenterAdminResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String country;

    private String city;

    private String address;

    private String phone;

    private String ssn;
}
