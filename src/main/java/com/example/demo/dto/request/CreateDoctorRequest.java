package com.example.demo.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateDoctorRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String country;

    private String city;

    private String address;

    private String phone;

    private String ssn;

    private String password;

    private String rePassword;

    private UUID examinationTypeId;

    private LocalTime startAt;

    private LocalTime endAt;
}
