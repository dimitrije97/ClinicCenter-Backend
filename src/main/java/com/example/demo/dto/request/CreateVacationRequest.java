package com.example.demo.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class CreateVacationRequest {

    private Date from;

    private Date until;
}
