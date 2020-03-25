package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateExaminationRequestByAdmin {

    private UUID examinationId;

    private UUID emergencyRoomId;
}
