package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeResponse {

    private UUID id;

    private String grade;

    private String doctorFirstName;

    private String doctorLastName;

    private String clinicName;

    private String patientFirstName;

    private String patientLastName;
}
