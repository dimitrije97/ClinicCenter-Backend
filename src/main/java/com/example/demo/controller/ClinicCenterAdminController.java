package com.example.demo.controller;

import com.example.demo.dto.request.UpdateClinicCenterAdminRequest;
import com.example.demo.dto.response.ClinicCenterAdminResponse;
import com.example.demo.repository.IClinicCenterAdminRepository;
import com.example.demo.service.IClinicCenterAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/clinic-center-admins")
public class ClinicCenterAdminController {

    private final IClinicCenterAdminService _clinicCenterAdminService;

    private final IClinicCenterAdminRepository _clinicCenterAdminRepository;

    public ClinicCenterAdminController(IClinicCenterAdminService clinicCenterAdminService, IClinicCenterAdminRepository clinicCenterAdminRepository) {
        _clinicCenterAdminService = clinicCenterAdminService;
        _clinicCenterAdminRepository = clinicCenterAdminRepository;
    }

    @GetMapping("/{id}/clinic-center-admin")
    public ClinicCenterAdminResponse getClinicCenterAdmin(@PathVariable UUID id) { return _clinicCenterAdminService.getClinicCenterAdmin(id); }

    @GetMapping()
    public Set<ClinicCenterAdminResponse> getAllClinicCetnerAdmins() { return _clinicCenterAdminService.getAllClinincCenterAdmins(); }

    @PutMapping("/{id}/clinic-center-admin")
    public ClinicCenterAdminResponse updateClinicCenterAdmin(@RequestBody UpdateClinicCenterAdminRequest request, @PathVariable UUID id) throws Exception { return _clinicCenterAdminService.updateClinicCenterAdmin(request, id); }
}
