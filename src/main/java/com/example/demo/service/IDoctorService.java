package com.example.demo.service;

import com.example.demo.dto.request.CreateDoctorRequest;
import com.example.demo.dto.request.SearchDoctorsRequest;
import com.example.demo.dto.request.UpdateDoctorRequest;
import com.example.demo.dto.response.DoctorResponse;

import java.util.Set;
import java.util.UUID;

public interface IDoctorService {

    DoctorResponse createDoctor(CreateDoctorRequest doctorRequest, UUID clinicId) throws Exception;

    DoctorResponse updateDoctor(UpdateDoctorRequest updateDoctorRequest, UUID id) throws Exception;

    void deleteDoctor(UUID id) throws Exception;

    DoctorResponse getDoctor(UUID id);

    Set<DoctorResponse> getAllDoctors() throws Exception;

    Set<DoctorResponse> getAllDoctorsOfClinic(UUID clinicId) throws Exception;

    Set<DoctorResponse> getAllDoctorsOfClinic(SearchDoctorsRequest request, UUID clinicId) throws Exception;
}
