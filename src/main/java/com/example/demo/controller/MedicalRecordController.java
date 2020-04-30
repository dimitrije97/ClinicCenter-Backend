package com.example.demo.controller;

import com.example.demo.dto.request.CreateMedicalRecordRequest;
import com.example.demo.dto.request.GetMedicalRecordRequest;
import com.example.demo.dto.request.UpdateMedicalRecordRequest;
import com.example.demo.dto.request.UpdateMedicineRequest;
import com.example.demo.dto.response.MedicalRecordResponse;
import com.example.demo.service.IMedicalRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("medical-records")
public class MedicalRecordController {

    private final IMedicalRecordService _medicalRecordService;

    public MedicalRecordController(IMedicalRecordService medicalRecordService) {
        _medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public MedicalRecordResponse createMedicalRecord(@RequestBody CreateMedicalRecordRequest request) throws Exception {
        return _medicalRecordService.createMedicalRecord(request);
    }

    @GetMapping
    public List<MedicalRecordResponse> getAllMedicalRecord() throws Exception {
        return _medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/patient")
    public MedicalRecordResponse getMedicalRecordByPatient(GetMedicalRecordRequest request) throws Exception {
        return _medicalRecordService.getMedicalRecordByPatient(request);
    }

    @PutMapping
    public MedicalRecordResponse updateMedicalRecord(@RequestBody UpdateMedicalRecordRequest request) throws Exception {
        return _medicalRecordService.updateMedicalRecord(request);
    }
}
