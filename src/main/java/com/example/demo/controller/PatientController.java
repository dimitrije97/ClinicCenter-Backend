package com.example.demo.controller;

import com.example.demo.dto.request.AdminsMessageAboutDenyingRegistrationRequest;
import com.example.demo.dto.request.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.entity.Patient;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.service.IPatientService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
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

    @DeleteMapping("/{id}/patient")
    public void deletePatient(@PathVariable UUID id) throws Exception { _patientService.deletePatient(id); }

    @GetMapping
    public Set<PatientResponse> getAllPatients() {
        return _patientService.getAllPatients();
    }

    @GetMapping("/requests")
    public Set<PatientResponse> getAllRequests() {
        return _patientService.getAllPendingRequests();
    }

    @PostMapping("/confirm/{id}/request")
    public PatientResponse confirmRegistrationRequest(@PathVariable UUID id){
        return _patientService.confirmRegistrationRequest(id);
    }

    @PostMapping("/deny/{id}/request")
    public void denyRegistrationRequest(@PathVariable UUID id, @RequestBody AdminsMessageAboutDenyingRegistrationRequest request){
        _patientService.denyRegistrationRequest(id, request);
    }

    @PostMapping("/approve/{id}/patient")
    public PatientResponse approveRegistration(@PathVariable UUID id) throws Exception {
        return _patientService.approveRegistration(id);
    }

    @GetMapping("/{id}/clinic")
    public Set<PatientResponse> getAllPatientsByClinic(@PathVariable UUID id) {
        return _patientService.getAllPatientsByClinic(id);
    }
}
