package com.example.demo.controller;

import com.example.demo.dto.request.AvailableClinicsRequest;
import com.example.demo.dto.request.AvailableDoctorsRequest;
import com.example.demo.dto.request.AvailableEmergencyRoomsRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.EmergencyRoomResponse;
import com.example.demo.service.IFilterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/filters")
public class FilterController {

    private final IFilterService _filterService;

    public FilterController(IFilterService filterService) {
        _filterService = filterService;
    }

    @GetMapping("/clinics")
    public Set<ClinicResponse> getAllClinicsByDateAndStartAtAndExaminationType(@RequestBody AvailableClinicsRequest request) throws Exception {
        return _filterService.getClinicsByDateAndStartAtAndExamnationType(request);
    }

    @GetMapping("/doctors")
    public Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinic(@RequestBody AvailableDoctorsRequest request) throws Exception {
        return _filterService.getDoctorsByDateAndStartAtAndExaminationTypeAndClinic(request);
    }

    @GetMapping("/emergency-rooms")
    public Set<EmergencyRoomResponse> getEmergencyRoomsByDateAndStartAtAndClinic(@RequestBody AvailableEmergencyRoomsRequest request) throws Exception {
        return _filterService.getEmergencyRoomsByDateAndStartAtAndClinic(request);
    }
}
