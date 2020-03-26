package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ExaminationResponse;

import java.util.Set;
import java.util.UUID;

public interface IExaminationService {

    ExaminationResponse createExaminationRequestByPatient(CreateExaminationRequestByPatient request);

    ExaminationResponse confirmExaminationRequestByAdmin(CreateExaminationRequestByAdmin request);

    ExaminationResponse approveExamination(ApproveExaminationRequest request);

    void denyExaminationRequest(ApproveExaminationRequest request);

    ExaminationResponse createPotentialExamination(CreatePotentialExaminationRequest request);

    Set<ExaminationResponse> getAllExaminations();

    ExaminationResponse getExamination(UUID id);

    Set<ExaminationResponse> getAllExaminationByPatient(UUID id);

    Set<ExaminationResponse> getAllExaminationByDoctor(UUID id);

    Set<ExaminationResponse> getAllPotentialExaminations();

    Set<ExaminationResponse> getAllPendingExaminations();

    Set<ExaminationResponse> getAllConfirmingExaminationsByPatient(UUID id);

    ExaminationResponse approvePotentialExamination(ApprovePotentialExaminationRequest request);

    ExaminationResponse createExaminationRequestByDoctor(CreateExaminationRequestByDoctor request, UUID id) throws Exception;
}
