package com.example.demo.controller;

import com.example.demo.dto.request.ApprovePotentialExaminationRequest;
import com.example.demo.dto.request.CreateExaminationRequestByDoctor;
import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.repository.IExaminationRepository;
import com.example.demo.service.IExaminationService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/potential-examinations")
public class PotentialExaminationController {

    private final IExaminationService _examinationService;

    public PotentialExaminationController(IExaminationService examinationService) {
        _examinationService = examinationService;
    }

    @PostMapping("/create-potential-examination")
    public ExaminationResponse createPotentialExaminationByDoctor(@RequestBody CreateExaminationRequestByDoctor request) {
        return _examinationService.createPotentialExaminationByDoctor(request);
    }

    @GetMapping()
    public Set<ExaminationResponse> getAllPotentialExaminations() {
        return _examinationService.getAllPotentialExaminations();
    }

    @PostMapping("/approve-potential-examination")
    public ExaminationResponse approvePotentialExamination(@RequestBody ApprovePotentialExaminationRequest request) {
        return _examinationService.approvePotentialExamination(request);
    }
}
