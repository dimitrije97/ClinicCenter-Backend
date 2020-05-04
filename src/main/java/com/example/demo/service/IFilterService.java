package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.EmergencyRoomResponse;

import java.util.Set;

public interface IFilterService {

    Set<ClinicResponse> getClinicsByDateAndExamnationType(AvailableClinicsRequest request) throws Exception;

    Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinic(AvailableDoctorsRequest request) throws Exception;

    Set<EmergencyRoomResponse> getAvailableEmergencyRooms(AvailableEmergencyRoomsRequest request) throws Exception;

    Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinicByFirstNameAndLastName(SearchAvailableDoctorsRequest request) throws Exception;

    Set<EmergencyRoomResponse> getAvailableEmergencyRooms(SearchAvailableEmergencyRoomsRequest request) throws Exception;
}
