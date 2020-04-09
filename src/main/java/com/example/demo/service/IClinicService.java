package com.example.demo.service;

import com.example.demo.dto.request.CreateClinicRequest;
import com.example.demo.dto.request.NewClinicAdminRequest;
import com.example.demo.dto.request.UpdateClinicRequest;
import com.example.demo.dto.response.ClinicResponse;

import java.util.Set;
import java.util.UUID;

public interface IClinicService {

    ClinicResponse createClinic(CreateClinicRequest request);

    ClinicResponse getClinic(UUID id);

    Set<ClinicResponse> getAllClinics() throws Exception;

    void deleteClinic(UUID id) throws Exception;

    ClinicResponse updateClinic(UpdateClinicRequest request, UUID id);

    void addNewClinicAdmin(NewClinicAdminRequest request);
}
