package com.example.demo.service;

import com.example.demo.dto.request.CreateAdminRequest;
import com.example.demo.dto.request.UpdateAdminRequest;
import com.example.demo.dto.response.AdminResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;
import java.util.UUID;

public interface IAdminService {

    AdminResponse createAdmin(CreateAdminRequest request) throws Exception;

    AdminResponse getAdmin(UUID id);

    void deleteAdmin(UUID id);

    AdminResponse updateAdmin(UpdateAdminRequest request, UUID id);

    Set<AdminResponse> getAllAdmins();

}
