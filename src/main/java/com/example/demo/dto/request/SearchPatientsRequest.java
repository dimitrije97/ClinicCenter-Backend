package com.example.demo.dto.request;

import lombok.Data;

@Data
public class SearchPatientsRequest {

    private String firstName;

    private String lastName;

    private String ssn;
}
