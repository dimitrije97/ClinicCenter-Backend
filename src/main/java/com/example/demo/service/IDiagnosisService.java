package com.example.demo.service;

import com.example.demo.dto.request.CreateDiagnosisRequest;
import com.example.demo.dto.request.UpdateDiagnosisRequest;
import com.example.demo.dto.response.DiagnosisResponse;

import java.util.List;
import java.util.UUID;

public interface IDiagnosisService  {

    DiagnosisResponse createDiagnosis(CreateDiagnosisRequest request) throws Exception;

    DiagnosisResponse updateDiagnosis(UpdateDiagnosisRequest request) throws Exception;

    void deleteDiagnosis(UUID id);

    List<DiagnosisResponse> getAllDiagnosis() throws Exception;
}
