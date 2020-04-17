package com.example.demo.controller;

import com.example.demo.dto.request.CreateDiagnosisRequest;
import com.example.demo.dto.request.UpdateDiagnosisRequest;
import com.example.demo.dto.response.DiagnosisResponse;
import com.example.demo.service.IDiagnosisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    private final IDiagnosisService _diagnosisService;

    public DiagnosisController(IDiagnosisService diagnosisService) {
        _diagnosisService = diagnosisService;
    }

    @PostMapping
    public DiagnosisResponse createDiagnosis(@RequestBody CreateDiagnosisRequest request) throws Exception {
        return _diagnosisService.createDiagnosis(request);
    }

    @PutMapping
    public  DiagnosisResponse updateDiagnosis(@RequestBody UpdateDiagnosisRequest request) throws Exception {
        return _diagnosisService.updateDiagnosis(request);
    }

    @DeleteMapping("/{id}/diagnosis")
    public void deleteDiagnosis(@PathVariable UUID id){
        _diagnosisService.deleteDiagnosis(id);
    }

    @GetMapping
    public List<DiagnosisResponse> getAllDiagnosis() throws Exception {
        return _diagnosisService.getAllDiagnosis();
    }
}
