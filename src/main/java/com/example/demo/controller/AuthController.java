package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.*;
import com.example.demo.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IPatientService _patientService;

    private final IAuthService _authService;

    private final IAdminService _adminService;

    private final IDoctorService _doctorService;

    private final IClinicService _clinicService;

    private final INurseService _nurseService;

    public AuthController(IPatientService patientService, IAuthService authService, IAdminService adminService, IDoctorService doctorService, IClinicService clinicService, INurseService nurseService) {
        _patientService = patientService;
        _authService = authService;
        _adminService = adminService;
        _doctorService = doctorService;
        _clinicService = clinicService;
        _nurseService = nurseService;
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
    public DoctorResponse createDoctor(@RequestBody CreateDoctorRequest doctorRequest, @PathVariable UUID clinicId) throws Exception {

        return _doctorService.createDoctor(doctorRequest, clinicId);
    }

    @PostMapping("/nurses")
    public NurseResponse createNurse(@RequestBody CreateNurseRequest nurseRequest, @PathVariable UUID clinicId) throws Exception {

        return _nurseService.createNurse(nurseRequest, clinicId);
    }

    @PostMapping("/clinics")
    public ClinicResponse createClinic(@RequestBody CreateClinicRequest request) { return  _clinicService.createClinic(request); }
}
