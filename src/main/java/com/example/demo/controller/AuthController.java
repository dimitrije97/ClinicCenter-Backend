package com.example.demo.controller;

import com.example.demo.dto.request.CreateAdminRequest;
import com.example.demo.dto.request.CreatePatientRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.NewPasswordRequest;
import com.example.demo.dto.response.AdminResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.service.IAdminService;
import com.example.demo.service.IAuthService;
import com.example.demo.service.IPatientService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IPatientService _patientService;

    private final IAuthService _authService;

    private final IAdminService _adminService;

    public AuthController(IPatientService patientService, IAuthService authService, IAdminService adminService) {
        _patientService = patientService;
        _authService = authService;
        _adminService = adminService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws Exception {
        return _authService.login(request);
    }

    @PostMapping("/{id}/new-password")
    public LoginResponse firstLogin(@PathVariable UUID id, @RequestBody NewPasswordRequest request) throws Exception {
        return _authService.setNewPassword(id, request);
    }

    @PostMapping("/patients")
    public PatientResponse createPatient(@RequestBody CreatePatientRequest request) {
        try {
            return _patientService.createPatient(request);
        } catch (Exception ex) {
            return null;
        }
    }

    @PostMapping("/admins")
    public AdminResponse createAdmin(@RequestBody CreateAdminRequest adminRequest) throws Exception {

        return _adminService.createAdmin(adminRequest);
    }
}
