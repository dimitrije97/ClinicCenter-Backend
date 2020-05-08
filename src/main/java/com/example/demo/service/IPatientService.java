package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.PatientResponse;

import java.util.Set;
import java.util.UUID;

public interface IPatientService {

    PatientResponse createPatient(CreatePatientRequest request) throws Exception;

    PatientResponse getPatient(UUID id);

    PatientResponse updatePatient(UpdatePatientRequest request, UUID id);

    void deletePatient(UUID id) throws Exception;

    Set<PatientResponse> getAllPatients() throws Exception;

    Set<PatientResponse> getAllPendingRequests() throws Exception;

    PatientResponse confirmRegistrationRequest(ApprovePatientRequest request);

    void denyRegistrationRequest(UUID patientId, AdminsMessageAboutDenyingRegistrationRequest request);

    PatientResponse approveRegistration(ApprovePatientRequest request) throws Exception;

    Set<PatientResponse> getAllPatientsByClinic(UUID clinicId);

    Set<PatientResponse> getAllPatientsWithoutMedicalRecord() throws Exception;

    Set<PatientResponse> getAllPatientsWithMedicalRecord() throws Exception;

    Set<PatientResponse> getAllPatientsByClinic(SearchPatientsRequest request, UUID clinicId) throws Exception;
}
