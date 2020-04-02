package com.example.demo.dto.response;

import com.example.demo.util.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String country;

    private String city;

    private String address;

    private String phone;

    private String ssn;

    private UserType userType;

    private boolean setNewPassword;

    private boolean deleted;

    private ClinicResponse myClinic;
}
