package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationResponse {

    private UUID scheduleId;

    private Date date;

    private String firstName;

    private String lastName;

    private String clinicName;
}
