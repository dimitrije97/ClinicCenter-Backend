package com.example.demo.dto.request;

import lombok.Data;

@Data
public class CreateClinicCenterAdminRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String country;

    private String city;

    private String address;

    private String phone;

    private String ssn;
}
