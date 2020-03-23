package com.example.demo.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class NewClinicAdminRequest {

    private UUID clinicId;

    private UUID adminId;
}
