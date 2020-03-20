package com.example.demo.controller;

import com.example.demo.dto.request.CreatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.service.IPatientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IPatientService _patientService;

    public AuthController(IPatientService patientService) {
        _patientService = patientService;
    }

    @PostMapping("/patients")
    public PatientResponse createPatient(@RequestBody CreatePatientRequest request) {
        try {
            return _patientService.createPatient(request);
        } catch (Exception ex) {
            return null;
        }
    }
}
