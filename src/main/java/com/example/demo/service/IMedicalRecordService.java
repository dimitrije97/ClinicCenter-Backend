package com.example.demo.service;

import com.example.demo.dto.request.CreateMedicalRecordRequest;
import com.example.demo.dto.request.GetMedicalRecordRequest;
import com.example.demo.dto.request.UpdateMedicalRecordRequest;
import com.example.demo.dto.response.MedicalRecordResponse;

import java.util.List;

public interface IMedicalRecordService {

    MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest recordRequest) throws Exception;

    List<MedicalRecordResponse> getAllMedicalRecords() throws Exception;

    MedicalRecordResponse getMedicalRecordByPatient(GetMedicalRecordRequest request) throws Exception;

    MedicalRecordResponse updateMedicalRecord(UpdateMedicalRecordRequest request) throws Exception;
}
