package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.EmergencyRoomResponse;
import com.example.demo.service.IFilterService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/filters")
public class FilterController {

    private final IFilterService _filterService;

    public FilterController(IFilterService filterService) {
        _filterService = filterService;
    }

    @GetMapping("/clinics")
    public Set<ClinicResponse> getAllClinicsByDateAndAndExaminationType(AvailableClinicsRequest request) throws Exception {
        return _filterService.getClinicsByDateAndExamnationType(request);
    }

    @GetMapping("/doctors")
    public Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinic(AvailableDoctorsRequest request) throws Exception {
        return _filterService.getDoctorsByDateAndStartAtAndExaminationTypeAndClinic(request);
    }

    @GetMapping("/emergency-rooms")
    public Set<EmergencyRoomResponse> getAvailableEmergencyRooms(AvailableEmergencyRoomsRequest request) throws Exception {
        return _filterService.getAvailableEmergencyRooms(request);
    }

    @GetMapping("/emergency-rooms-operations")
    public Set<EmergencyRoomResponse> getAvailableEmergencyRoomsOp(AvailableEmergencyRoomsRequest request) throws Exception {
        return _filterService.getAvailableEmergencyRoomsOp(request);
    }

    @GetMapping("/doctors/search")
    public Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinicByFirstNameAndLastName(SearchAvailableDoctorsRequest request) throws Exception {
        return _filterService.getDoctorsByDateAndStartAtAndExaminationTypeAndClinicByFirstNameAndLastName(request);
    }

    @GetMapping("/emergency-rooms/search")
    public Set<EmergencyRoomResponse> getAvailableEmergencyRoomsByNameAndNumber(SearchAvailableEmergencyRoomsRequest request) throws Exception {
        return _filterService.getAvailableEmergencyRooms(request);
    }
}
