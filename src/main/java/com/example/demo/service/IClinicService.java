package com.example.demo.service;

import com.example.demo.dto.request.CreateClinicRequest;
import com.example.demo.dto.request.NewClinicAdminRequest;
import com.example.demo.dto.request.SearchClinicsRequest;
import com.example.demo.dto.request.UpdateClinicRequest;
import com.example.demo.dto.response.ClinicResponse;

import java.util.Set;
import java.util.UUID;

public interface IClinicService {

    ClinicResponse createClinic(CreateClinicRequest request) throws Exception;

    ClinicResponse getClinic(UUID id);

    Set<ClinicResponse> getAllClinics() throws Exception;

    void deleteClinic(UUID id) throws Exception;

    ClinicResponse updateClinic(UpdateClinicRequest request, UUID id) throws Exception;

    void addNewClinicAdmin(NewClinicAdminRequest request);

    Set<ClinicResponse> getAllClinics(SearchClinicsRequest request) throws Exception;
}
