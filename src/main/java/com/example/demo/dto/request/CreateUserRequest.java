package com.example.demo.dto.request;

import com.example.demo.util.enums.UserType;
import lombok.Data;

@Data
public class CreateUserRequest {

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

    private UserType userType;
}
