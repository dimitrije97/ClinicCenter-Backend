package com.example.demo.service;

import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.PatientResponse;

import java.util.Set;
import java.util.UUID;

public interface IScheduleService {

    Set<PatientResponse> getAllPateintsByDoctor(UUID id);

    Set<DoctorResponse> getAllDoctorsByPatient(UUID id);

    Set<ClinicResponse> getAllClinicsByPatient(UUID id);
}
