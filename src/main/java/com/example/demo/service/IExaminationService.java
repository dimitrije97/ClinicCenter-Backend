package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ExaminationResponse;

import java.util.Set;
import java.util.UUID;

public interface IExaminationService {

    ExaminationResponse createExaminationRequestByPatient(CreateExaminationRequestByPatient request) throws Exception;

    ExaminationResponse confirmExaminationRequestByAdmin(CreateExaminationRequestByAdmin request);

    ExaminationResponse approveExamination(ApproveExaminationRequest request) throws Exception;

    void denyExaminationRequest(DenyExaminationRequest request);

    ExaminationResponse createPotentialExamination(CreatePotentialExaminationRequest request) throws Exception;

    Set<ExaminationResponse> getAllExaminations();

    ExaminationResponse getExamination(UUID id);

    Set<ExaminationResponse> getAllExaminationByPatient(UUID id);

    Set<ExaminationResponse> getAllExaminationByDoctor(UUID id);

    Set<ExaminationResponse> getAllPotentialExaminations() throws Exception;

    Set<ExaminationResponse> getAllPendingExaminations() throws Exception;

    Set<ExaminationResponse> getAllConfirmingExaminationsByPatient(UUID id) throws Exception;

    ExaminationResponse approvePotentialExamination(ApprovePotentialExaminationRequest request) throws Exception;

    ExaminationResponse createExaminationRequestByDoctor(CreateExaminationRequestByDoctor request, UUID id) throws Exception;

    Set<ExaminationResponse> getAllPotentialExaminationsByClinic(UUID clinicId) throws Exception;

    void deletePotentialExamination(UUID id);

    Set<ExaminationResponse> getAllPendingExaminationsByClinic(UUID clinicId) throws Exception;

    void cancelExamination(UUID id) throws Exception;

    Set<ExaminationResponse> getPatientsExaminationHistory(UUID patientId) throws Exception;

    Set<ExaminationResponse> getExaminationsWhichPatientCanCancel(UUID patientId) throws Exception;

    Set<ExaminationResponse> getExaminationsWhichDoctorCanCancel(UUID doctorId) throws Exception;
}
