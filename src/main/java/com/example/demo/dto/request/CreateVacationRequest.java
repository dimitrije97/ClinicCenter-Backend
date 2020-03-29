package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateVacationRequest {

    @JsonFormat(pattern = "yyyy/MM/dd")
    private List<Date> dates;
}
