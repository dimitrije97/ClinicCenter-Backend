package com.example.demo.service;

import com.example.demo.dto.request.CreateEmergencyRoomRequest;
import com.example.demo.dto.request.UpdateEmergencyRoomRequest;
import com.example.demo.dto.response.EmergencyRoomResponse;

import java.util.Set;
import java.util.UUID;

public interface IEmergencyRoomService {

    EmergencyRoomResponse createEmergencyRoom(CreateEmergencyRoomRequest request, UUID id) throws Exception;

    EmergencyRoomResponse updateEmergencyRoom(UpdateEmergencyRoomRequest request, UUID id) throws Exception;

    EmergencyRoomResponse getEmergencyRoom(UUID id);

    Set<EmergencyRoomResponse> getAllEmergencyRooms();

    Set<EmergencyRoomResponse> getAllEmergencyRoomsOfClinic(UUID id);

    void deleteEmergencyRoom(UUID id);
}
