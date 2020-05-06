package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class DenyOperationRequest {

    private UUID examinationId;

    private String reason;
}
