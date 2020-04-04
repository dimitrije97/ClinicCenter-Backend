package com.example.demo.controller;

import com.example.demo.dto.request.ApprovePotentialExaminationRequest;
import com.example.demo.dto.request.CreatePotentialExaminationRequest;
import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.service.IExaminationService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/potential-examinations")
public class PotentialExaminationController {

    private final IExaminationService _examinationService;

    public PotentialExaminationController(IExaminationService examinationService) {
        _examinationService = examinationService;
    }

    @PostMapping("/create-potential-examination")
    public ExaminationResponse createPotentialExamination(@RequestBody CreatePotentialExaminationRequest request) throws Exception {
        return _examinationService.createPotentialExamination(request);
    }

    @GetMapping()
    public Set<ExaminationResponse> getAllPotentialExaminations() {
        return _examinationService.getAllPotentialExaminations();
    }

    @PostMapping("/approve-potential-examination")
    public ExaminationResponse approvePotentialExamination(@RequestBody ApprovePotentialExaminationRequest request) throws Exception {
        return _examinationService.approvePotentialExamination(request);
    }

    @GetMapping("/{id}/clinic")
    public Set<ExaminationResponse> getAllPotentialExaminationsByClinic(@PathVariable UUID id) {
        return _examinationService.getAllPotentialExaminationsByClinic(id);
    }
}
