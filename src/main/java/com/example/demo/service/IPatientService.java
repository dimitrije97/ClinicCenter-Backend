package com.example.demo.service;

import com.example.demo.dto.request.AdminsMessageAboutDenyingRegistrationRequest;
import com.example.demo.dto.request.CreatePatientRequest;
import com.example.demo.dto.request.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IPatientService {

    PatientResponse createPatient(CreatePatientRequest request) throws Exception;

    PatientResponse getPatient(UUID id);

    PatientResponse updatePatient(UpdatePatientRequest request, UUID id);

    void deletePatient(UUID id) throws Exception;

    Set<PatientResponse> getAllPatients();

    Set<PatientResponse> getAllPendingRequests();

    PatientResponse confirmRegistrationRequest(UUID patientId);

    void denyRegistrationRequest(UUID patientId, AdminsMessageAboutDenyingRegistrationRequest request);

    PatientResponse approveRegistration(UUID patientId) throws Exception;

    Set<PatientResponse> getAllPatientsByClinic(UUID clinicId);
}
