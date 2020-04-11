package com.example.demo.dto.response;

import com.example.demo.util.enums.ReasonOfUnavailability;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse {

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date date;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startAt;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endAt;

    private ReasonOfUnavailability reasonOfUnavailability;

    private UUID id;
}
