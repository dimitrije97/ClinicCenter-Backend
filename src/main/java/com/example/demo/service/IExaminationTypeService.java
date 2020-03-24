package com.example.demo.service;

import com.example.demo.dto.request.CreateExaminationTypeRequest;
import com.example.demo.dto.request.UpdateExaminationRequest;
import com.example.demo.dto.response.ExaminationTypeResponse;

import java.util.Set;
import java.util.UUID;

public interface IExaminationTypeService {

    ExaminationTypeResponse createExaminationType(CreateExaminationTypeRequest request) throws Exception;

    ExaminationTypeResponse updateExaminationType(UpdateExaminationRequest request, UUID id) throws Exception;

    ExaminationTypeResponse getExaminationType(UUID id);

    Set<ExaminationTypeResponse> getAllExaminationTypes();

    Set<ExaminationTypeResponse> getAllExaminationTypesOfClinic(UUID clinicId);

    void deleteExaminationType(UUID id);
}
