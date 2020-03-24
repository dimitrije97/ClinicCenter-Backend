package com.example.demo.controller;

import com.example.demo.dto.request.UpdateExaminationRequest;
import com.example.demo.dto.response.ExaminationTypeResponse;
import com.example.demo.repository.IExaminationTypeRepository;
import com.example.demo.service.IExaminationTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/examination-types")
public class ExaminationTypeController {

    private final IExaminationTypeService _examinationTypeService;

    private final IExaminationTypeRepository _examinationTypeRepository;

    public ExaminationTypeController(IExaminationTypeService examinationTypeService, IExaminationTypeRepository examinationTypeRepository) {
        _examinationTypeService = examinationTypeService;
        _examinationTypeRepository = examinationTypeRepository;
    }

    @GetMapping("/{id}/examination-type")
    public ExaminationTypeResponse getExaminationType(@PathVariable UUID id) { return _examinationTypeService.getExaminationType(id); }

    @GetMapping()
    public Set<ExaminationTypeResponse> getAllExaminationTypes() { return _examinationTypeService.getAllExaminationTypes(); }

    @DeleteMapping("/{id}/examination-type")
    public void deleteExaminationType(@PathVariable UUID id) {
        _examinationTypeService.deleteExaminationType(id);
    }

    @PutMapping("/{id}/examination-type")
    public ExaminationTypeResponse updateExaminationType(@RequestBody UpdateExaminationRequest request, @PathVariable UUID id) throws Exception { return _examinationTypeService.updateExaminationType(request, id); }

    @GetMapping("/{id}/clinic")
    public Set<ExaminationTypeResponse> getExaminationTypesOfClinic(@PathVariable UUID id) { return _examinationTypeService.getAllExaminationTypesOfClinic(id); }

}
