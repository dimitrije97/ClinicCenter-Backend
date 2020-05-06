package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.service.IExaminationService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/operations")
public class OperationController {

    private final IExaminationService _examinationService;

    public OperationController(IExaminationService examinationService) {
        _examinationService = examinationService;
    }

    @PostMapping("/create-operation-request")
    public ExaminationResponse createOperationRequest(@RequestBody CreateOperationRequest request) throws Exception {
        return _examinationService.createOperationRequest(request);
    }

    @PutMapping("/approve-operation")
    public ExaminationResponse approveOperation(@RequestBody ApproveOperationRequest request) throws Exception {
        return _examinationService.approveOperation(request);
    }

    @PutMapping("/deny-operation")
    public void denyOperation(@RequestBody DenyOperationRequest request) throws Exception {
        _examinationService.denyOperation(request);
    }

    @DeleteMapping("/cancel/{id}/operation")
    public void cancelOperation(@PathVariable UUID id) throws Exception {
        _examinationService.cancelOperation(id);
    }

    @GetMapping("/pending")
    public Set<ExaminationResponse> getAllPendingOperations() throws Exception {
        return _examinationService.getAllPendingOperations();
    }

    @GetMapping("/pending/{id}/clinic")
    public Set<ExaminationResponse> getAllPendingOperationsByClinic(@PathVariable UUID id) throws Exception {
        return _examinationService.getAllPendingOperationsByClinic(id);
    }

    @GetMapping("/history/{id}/patient")
    public Set<ExaminationResponse> getPatientsOperationHistory(@PathVariable UUID id) throws Exception {
        return _examinationService.getPatientsOperationHistory(id);
    }

    @GetMapping("/future/{id}/patient")
    public Set<ExaminationResponse> getAllOperationsWhichPatientCanCancel(@PathVariable UUID id) throws Exception {
        return _examinationService.getOperationsWhichPatientCanCancel(id);
    }

    @GetMapping("/future/{id}/doctor")
    public Set<ExaminationResponse> getAllOperationsWhichDoctorCanCancel(@PathVariable UUID id) throws Exception {
        return _examinationService.getOperationsWhichDoctorCanCancel(id);
    }
}
