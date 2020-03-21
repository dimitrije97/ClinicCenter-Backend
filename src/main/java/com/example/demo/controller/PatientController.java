package com.example.demo.controller;

import com.example.demo.dto.request.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.entity.Patient;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.service.IPatientService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final IPatientService _patientService;

    private final IPatientRepository _patientRepository;

    public PatientController(IPatientService patientService, IPatientRepository patientRepository) {
        _patientService = patientService;
        _patientRepository = patientRepository;
    }

    @PutMapping("/{id}/patient")
    public PatientResponse updatePatient(@RequestBody UpdatePatientRequest request, @PathVariable UUID id) {
        return _patientService.updatePatient(request, id);
    }

    @GetMapping("/{id}/patient")
    public PatientResponse getPatient(@PathVariable UUID id) {
        return _patientService.getPatient(id);
    }
}
