package com.example.demo.service;

import com.example.demo.dto.request.AvailableClinicsRequest;
import com.example.demo.dto.request.AvailableDoctorsRequest;
import com.example.demo.dto.request.AvailableEmergencyRoomsRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.EmergencyRoomResponse;

import java.util.Set;

public interface IFilterService {

    Set<ClinicResponse> getClinicsByDateAndStartAtAndExamnationType(AvailableClinicsRequest request) throws Exception;

    Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinic(AvailableDoctorsRequest request) throws Exception;

    Set<EmergencyRoomResponse> getEmergencyRoomsByDateAndStartAtAndClinic(AvailableEmergencyRoomsRequest request) throws Exception;
}