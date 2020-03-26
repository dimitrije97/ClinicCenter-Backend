package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationResponse {

    private String doctorFirstName;

    private String doctorLastName;

    private String clinicName;

    private String from;

    private String until;
}
