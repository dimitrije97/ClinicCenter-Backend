package com.example.demo.service;

import com.example.demo.dto.request.CreateClinicCenterAdminRequest;
import com.example.demo.dto.request.UpdateClinicCenterAdminRequest;
import com.example.demo.dto.response.ClinicCenterAdminResponse;

import java.util.Set;
import java.util.UUID;

public interface IClinicCenterAdminService {

    ClinicCenterAdminResponse createClinicCenterAdmin(CreateClinicCenterAdminRequest request) throws Exception;

    ClinicCenterAdminResponse updateClinicCenterAdmin(UpdateClinicCenterAdminRequest request, UUID id) throws Exception;

    ClinicCenterAdminResponse getClinicCenterAdmin(UUID id);

    Set<ClinicCenterAdminResponse> getAllClinincCenterAdmins();
}
