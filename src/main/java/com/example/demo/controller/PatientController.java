package com.example.demo.controller;

import com.example.demo.dto.request.AdminsMessageAboutDenyingRegistrationRequest;
import com.example.demo.dto.request.ApprovePatientRequest;
import com.example.demo.dto.request.SearchPatientsRequest;
import com.example.demo.dto.request.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
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
    public Set<PatientResponse> getAllPatients() throws Exception {
        return _patientService.getAllPatients();
    }

    @GetMapping("/requests")
    public Set<PatientResponse> getAllRequests() throws Exception {
        return _patientService.getAllPendingRequests();
    }

    @PutMapping("/confirm")
    public PatientResponse confirmRegistrationRequest(@RequestBody ApprovePatientRequest request){
        return _patientService.confirmRegistrationRequest(request);
    }

    @PutMapping("/deny/{id}/request")
    public void denyRegistrationRequest(@PathVariable UUID id, @RequestBody AdminsMessageAboutDenyingRegistrationRequest request){
        _patientService.denyRegistrationRequest(id, request);
    }

    @PutMapping("/approve")
    public PatientResponse approveRegistration(@RequestBody ApprovePatientRequest request) throws Exception {
        return _patientService.approveRegistration(request);
    }

    @GetMapping("/{id}/clinic")
    public Set<PatientResponse> getAllPatientsByClinic(@PathVariable UUID id) {
        return _patientService.getAllPatientsByClinic(id);
    }

    @GetMapping("with-medical-record")
    public Set<PatientResponse> getAllPatientsWithMedicalRecord() throws Exception {
        return _patientService.getAllPatientsWithMedicalRecord();
    }

    @GetMapping("without-medical-record")
    public Set<PatientResponse> getAllPatientsWithoutMedicalRecord() throws Exception {
        return _patientService.getAllPatientsWithoutMedicalRecord();
    }

    @GetMapping("/search/{id}/clinic")
    public Set<PatientResponse> getAllPatientsByFirstNameAndLastNameAndSsn(SearchPatientsRequest request, @PathVariable UUID id) throws Exception {
        return _patientService.getAllPatientsByClinic(request, id);
    }
}
