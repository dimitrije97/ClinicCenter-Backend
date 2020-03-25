package com.example.demo.controller;

import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.service.IScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final IScheduleService _scheduleService;

    public ScheduleController(IScheduleService scheduleService) {
        _scheduleService = scheduleService;
    }

    @GetMapping("/doctors/{id}/patient")
    public Set<DoctorResponse> getAllDoctorsByPatient(@PathVariable UUID id) {
        return _scheduleService.getAllDoctorsByPatient(id);
    }

    @GetMapping("/patients/{id}/doctor")
    public Set<PatientResponse> getAllPatientsByDoctor(@PathVariable UUID id) {
        return _scheduleService.getAllPateintsByDoctor(id);
    }

    @GetMapping("/clinics/{id}/patient")
    public Set<ClinicResponse> getAllClinicsByPatient(@PathVariable UUID id) {
        return _scheduleService.getAllClinicsByPatient(id);
    }
}
