package com.example.demo.controller;

import com.example.demo.dto.request.UpdateDoctorRequest;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.service.implementation.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
public class DoctorContoller {

    private final DoctorService _doctorService;

    private final IDoctorRepository _doctorRepository;

    public DoctorContoller(DoctorService doctorService, IDoctorRepository doctorRepository) {
        _doctorService = doctorService;
        _doctorRepository = doctorRepository;
    }

    @GetMapping("/{id}/doctor")
    public DoctorResponse getDoctor(@PathVariable UUID id) {
        return _doctorService.getDoctor(id);
    }

    @GetMapping()
    public Set<DoctorResponse> getAllDoctors() {
        return _doctorService.getAllDoctors();
    }

    @GetMapping("/{id}/clinic")
    public Set<DoctorResponse> getAllDoctorsOfClinic(@PathVariable UUID id) { return _doctorService.getAllDoctorsOfClinic(id); }

    @DeleteMapping("/{id}/doctor")
    public void deleteDoctor(@PathVariable UUID id) throws Exception { _doctorService.deleteDoctor(id); }

    @PutMapping("/{id}/doctor")
    public DoctorResponse updateDoctor(@RequestBody UpdateDoctorRequest request, @PathVariable UUID id) throws Exception { return _doctorService.updateDoctor(request, id); }

}
