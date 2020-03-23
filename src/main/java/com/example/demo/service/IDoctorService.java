package com.example.demo.service;

import com.example.demo.dto.request.CreateDoctorRequest;
import com.example.demo.dto.request.UpdateDoctorRequest;
import com.example.demo.dto.response.DoctorResponse;

import java.util.Set;
import java.util.UUID;

public interface IDoctorService {

    DoctorResponse createDoctor(CreateDoctorRequest doctorRequest) throws Exception;

    DoctorResponse updateDoctor(UpdateDoctorRequest updateDoctorRequest, UUID id) throws Exception;

    void deleteDoctor(UUID id);

    DoctorResponse getDoctor(UUID id);

    Set<DoctorResponse> getAllDoctors();
}
