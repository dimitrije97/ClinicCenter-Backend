package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.AdminResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.service.IAdminService;
import com.example.demo.service.IAuthService;
import com.example.demo.service.IDoctorService;
import com.example.demo.service.IPatientService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IPatientService _patientService;

    private final IAuthService _authService;

    private final IAdminService _adminService;

    private final IDoctorService _doctorService;

    public AuthController(IPatientService patientService, IAuthService authService, IAdminService adminService, IDoctorService doctorService) {
        _patientService = patientService;
        _authService = authService;
        _adminService = adminService;
        _doctorService = doctorService;
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

    @PostMapping("/doctors")
    public DoctorResponse createDoctor(@RequestBody CreateDoctorRequest doctorRequest) throws Exception {

        return _doctorService.createDoctor(doctorRequest);
    }
}
