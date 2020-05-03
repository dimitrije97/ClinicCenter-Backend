package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.service.IExaminationService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/examinations")
public class ExaminationController {

    private final IExaminationService _examinationService;

    public ExaminationController(IExaminationService examinationService) {
        _examinationService = examinationService;
    }

    @PostMapping("/create-examination-request")
    public ExaminationResponse createExaminationRequestByPatient(@RequestBody CreateExaminationRequestByPatient request) throws Exception {
        return _examinationService.createExaminationRequestByPatient(request);
    }

    @PostMapping("/confirm-examination-request")
    public ExaminationResponse confirmExaminationRequestByAdmin(@RequestBody CreateExaminationRequestByAdmin request) {
        return _examinationService.confirmExaminationRequestByAdmin(request);
    }

    @PostMapping("/approve-examination")
    public ExaminationResponse approveExamination(@RequestBody ApproveExaminationRequest request) throws Exception {
        return _examinationService.approveExamination(request);
    }

    @PostMapping("/deny-examination")
    public void denyExamination(@RequestBody DenyExaminationRequest request) {
        _examinationService.denyExaminationRequest(request);
    }

    @GetMapping("/{id}/examination")
    public ExaminationResponse getExamination(@PathVariable UUID id) {
        return _examinationService.getExamination(id);
    }

    @GetMapping()
    public Set<ExaminationResponse> getAllExaminations() {
        return _examinationService.getAllExaminations();
    }

    @GetMapping("/{id}/patient")
    public Set<ExaminationResponse> getAllExaminationsByPatient(@PathVariable UUID id) {
        return _examinationService.getAllExaminationByPatient(id);
    }

    @GetMapping("/{id}/doctor")
    public Set<ExaminationResponse> getAllExaminationsByDoctor(@PathVariable UUID id) {
        return _examinationService.getAllExaminationByDoctor(id);
    }

    @GetMapping("/pending")
    public Set<ExaminationResponse> getAllPendingExaminations() throws Exception {
        return _examinationService.getAllPendingExaminations();
    }

    @GetMapping("/pending/{id}/clinic")
    public Set<ExaminationResponse> getAllPendingExaminationsByClinic(@PathVariable UUID id) throws Exception {
        return _examinationService.getAllPendingExaminationsByClinic(id);
    }

    @GetMapping("/confirming/{id}/patient")
    public Set<ExaminationResponse> getAllConfirmingExaminationsByPatient(@PathVariable UUID id) throws Exception {
        return _examinationService.getAllConfirmingExaminationsByPatient(id);
    }

    @PostMapping("/create-examination-request/{id}/doctor")
    public ExaminationResponse createExaminationRequestByDoctor(@RequestBody CreateExaminationRequestByDoctor request, @PathVariable UUID id) throws Exception {
        return _examinationService.createExaminationRequestByDoctor(request, id);
    }

    @DeleteMapping("/cancel/{id}/examination")
    public void cancelExamination(@PathVariable UUID id) throws Exception {
        _examinationService.cancelExamination(id);
    }

    @GetMapping("/history/{id}/patient")
    public Set<ExaminationResponse> getPatientsExaminationHistory(@PathVariable UUID id) throws Exception {
        return _examinationService.getPatientsExaminationHistory(id);
    }

    @GetMapping("/future/{id}/patient")
    public Set<ExaminationResponse> getAllExaminationsWhichPatientCanCancel(@PathVariable UUID id) throws Exception {
        return _examinationService.getExaminationsWhichPatientCanCancel(id);
    }

    @GetMapping("/future/{id}/doctor")
    public Set<ExaminationResponse> getAllExaminationsWhichDoctorCanCancel(@PathVariable UUID id) throws Exception {
        return _examinationService.getExaminationsWhichDoctorCanCancel(id);
    }

    @GetMapping("/search/history/{id}/patient")
    public Set<ExaminationResponse> getPatientsExaminationHistoryByName(SearchPatientsExaminationHistoryRequest request, @PathVariable UUID id) throws Exception {
        return _examinationService.getPatientsExaminationHistory(request, id);
    }
}
