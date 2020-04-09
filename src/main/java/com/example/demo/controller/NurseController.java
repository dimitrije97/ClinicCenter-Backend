package com.example.demo.controller;

import com.example.demo.dto.request.UpdateDoctorRequest;
import com.example.demo.dto.request.UpdateNurseRequest;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.NurseResponse;
import com.example.demo.service.INurseService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/nurses")
public class NurseController {

    private final INurseService _nurseService;

    public NurseController(INurseService nurseService) {
        _nurseService = nurseService;
    }

    @GetMapping("/{id}/nurse")
    public NurseResponse getNurse(@PathVariable UUID id) {
        return _nurseService.getNurse(id);
    }

    @GetMapping()
    public Set<NurseResponse> getAllNurses() throws Exception {
        return _nurseService.getAllNurses();
    }

    @GetMapping("/{id}/clinic")
    public Set<NurseResponse> getAllNursesOfClinic(@PathVariable UUID id) throws Exception { return _nurseService.getAllNursesOfClinic(id); }

    @DeleteMapping("/{id}/nurse")
    public void deleteNurse(@PathVariable UUID id) {
        _nurseService.deleteNurse(id);
    }

    @PutMapping("/{id}/nurse")
    public NurseResponse updateNurse(@RequestBody UpdateNurseRequest request, @PathVariable UUID id) throws Exception { return _nurseService.updateNurse(request, id); }

}
