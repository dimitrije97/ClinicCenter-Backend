package com.example.demo.controller;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.GradeResponse;
import com.example.demo.service.IGradeService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final IGradeService _gradeService;

    public GradeController(IGradeService gradeService) {
        _gradeService = gradeService;
    }

    @PostMapping("/grade-doctor")
    public GradeResponse gradeDoctor(@RequestBody GradeRequest request) throws Exception{
        return _gradeService.gradeDoctor(request);
    }

    @PostMapping("/grade-clinic")
    public GradeResponse gradeClinic(@RequestBody GradeRequest request) throws Exception{
        return _gradeService.gradeClinic(request);
    }

    @GetMapping("/{id}/doctor")
    public Set<GradeResponse> getAllGradesByDoctor(@PathVariable UUID id) throws Exception{
        return _gradeService.getAllGradesByDoctor(id);
    }

    @GetMapping("/{id}/clinic")
    public Set<GradeResponse> getAllGradesByClinic(@PathVariable UUID id) throws Exception{
        return _gradeService.getAllGradesByClinic(id);
    }

    @GetMapping("/doctor/{id}/patient")
    public Set<GradeResponse> getAllGradedDoctorsByPatient(@PathVariable UUID id) throws Exception{
        return _gradeService.getAllGradedDoctorsPatient(id);
    }

    @GetMapping("/clinic/{id}/patient")
    public Set<GradeResponse> getAllGradedClinicsByPatient(@PathVariable UUID id) throws Exception{
        return _gradeService.getAllGradedClinicsPatient(id);
    }

    @GetMapping("/avg/{id}/doctor")
    public GradeResponse getAvgGradeByDoctor(@PathVariable UUID id) throws Exception{
        return _gradeService.getAvgGradeByDoctor(id);
    }

    @GetMapping("/avg/{id}/clinic")
    public GradeResponse getAvgGradeByClinic(@PathVariable UUID id) throws Exception{
        return _gradeService.getAvgGradeByClinic(id);
    }

    @GetMapping("/doctors/{id}/patient")
    public Set<DoctorResponse> getAllDoctorsWhoCanBeGradedByPatient(@PathVariable UUID id) throws Exception{
        return _gradeService.getAllDoctorsWhoCanBeGradedByPatient(id);
    }

    @GetMapping("/clinics/{id}/patient")
    public Set<ClinicResponse> getAllClinicsWhichCanBeGradedByPatient(@PathVariable UUID id) throws Exception{
        return _gradeService.getAllClinicsWhichCanBeGradedByPatient(id);
    }
}
