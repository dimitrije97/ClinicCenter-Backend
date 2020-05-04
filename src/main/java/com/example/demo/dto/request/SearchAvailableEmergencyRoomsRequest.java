package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SearchAvailableEmergencyRoomsRequest {

    private UUID examinationId;

    private String name;

    private String number;
}
