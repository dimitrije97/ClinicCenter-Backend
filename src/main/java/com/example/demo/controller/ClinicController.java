package com.example.demo.controller;

import com.example.demo.dto.request.NewClinicAdminRequest;
import com.example.demo.dto.request.SearchClinicsRequest;
import com.example.demo.dto.request.UpdateClinicRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.service.IClinicService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/clinics")
public class ClinicController {

    private final IClinicRepository _clinicRepository;

    private final IClinicService _clinicService;

    public ClinicController(IClinicRepository clinicRepository, IClinicService clinicService) {
        _clinicRepository = clinicRepository;
        _clinicService = clinicService;
    }

    @GetMapping("/{id}/clinic")
    public ClinicResponse getClinic(@PathVariable UUID id) {
        return _clinicService.getClinic(id);
    }

    @GetMapping()
    public Set<ClinicResponse> getAllClinics() throws Exception {
        return _clinicService.getAllClinics();
    }

    @DeleteMapping("/{id}/clinic")
    public void deleteClinic(@PathVariable UUID id) throws Exception { _clinicService.deleteClinic(id); }

    @PutMapping("/{id}/clinic")
    public ClinicResponse updateClinic(@RequestBody UpdateClinicRequest request, @PathVariable UUID id) throws Exception { return _clinicService.updateClinic(request, id); }

    @PutMapping("/add-clinic-admin")
    public void addClinicAdmin(@RequestBody NewClinicAdminRequest request) {  _clinicService.addNewClinicAdmin(request); }

    @GetMapping("/search")
    public Set<ClinicResponse> getAllClinicsByNameAndAddress(SearchClinicsRequest request) throws Exception {
        return _clinicService.getAllClinics(request);
    }
}
