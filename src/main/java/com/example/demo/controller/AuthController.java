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

    private final IClinicCenterAdminService _clinicCenterAdminService;

    private final IEmergencyRoomService _emergencyRoomService;

    private final IExaminationTypeService _examinationTypeService;

    public AuthController(IPatientService patientService, IAuthService authService, IAdminService adminService, IDoctorService doctorService, IClinicService clinicService, INurseService nurseService, IClinicCenterAdminService clinicCenterAdminService, IEmergencyRoomService emergencyRoomService, IExaminationTypeService examinationTypeService) {
        _patientService = patientService;
        _authService = authService;
        _adminService = adminService;
        _doctorService = doctorService;
        _clinicService = clinicService;
        _nurseService = nurseService;
        _clinicCenterAdminService = clinicCenterAdminService;
        _emergencyRoomService = emergencyRoomService;
        _examinationTypeService = examinationTypeService;
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
    public PatientResponse createPatient(@RequestBody CreatePatientRequest request) throws Exception {
        return _patientService.createPatient(request);
    }

    @PostMapping("/admins")
    public AdminResponse createAdmin(@RequestBody CreateAdminRequest adminRequest) throws Exception {

        return _adminService.createAdmin(adminRequest);
    }

    @PostMapping("/doctors/{id}/clinic")
    public DoctorResponse createDoctor(@RequestBody CreateDoctorRequest doctorRequest, @PathVariable UUID id) throws Exception {

        return _doctorService.createDoctor(doctorRequest, id);
    }

    @PostMapping("/nurses/{id}/clinic")
    public NurseResponse createNurse(@RequestBody CreateNurseRequest nurseRequest, @PathVariable UUID id) throws Exception {

        return _nurseService.createNurse(nurseRequest, id);
    }

    @PostMapping("/clinic-center-admins")
    public ClinicCenterAdminResponse createClinicCenterAdmin(@RequestBody CreateClinicCenterAdminRequest clinicCenterAdminRequest) throws Exception {

        return _clinicCenterAdminService.createClinicCenterAdmin(clinicCenterAdminRequest);
    }

    @PostMapping("/clinics")
    public ClinicResponse createClinic(@RequestBody CreateClinicRequest request) {

        return  _clinicService.createClinic(request);
    }

    @PostMapping("/emergency-rooms/{id}/clinic")
    public EmergencyRoomResponse createEmergencyRoom(@RequestBody CreateEmergencyRoomRequest request, @PathVariable UUID id) throws Exception {

        return _emergencyRoomService.createEmergencyRoom(request, id);
    }

    @PostMapping("/examination-types")
    public ExaminationTypeResponse createExaminationType(@RequestBody CreateExaminationTypeRequest request) throws Exception {

        return _examinationTypeService.createExaminationType(request);
    }
}
