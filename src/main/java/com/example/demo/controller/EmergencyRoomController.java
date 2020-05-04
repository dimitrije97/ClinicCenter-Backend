package com.example.demo.controller;

import com.example.demo.dto.request.SearchEmergencyRoomsRequest;
import com.example.demo.dto.request.UpdateEmergencyRoomRequest;
import com.example.demo.dto.response.EmergencyRoomResponse;
import com.example.demo.service.IEmergencyRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("emergency-rooms")
public class EmergencyRoomController {

    private final IEmergencyRoomService _emergencyRoomService;

    public EmergencyRoomController(IEmergencyRoomService emergencyRoomService) {
        _emergencyRoomService = emergencyRoomService;
    }

    @PutMapping("/{id}/emergency-room")
    public EmergencyRoomResponse updateEmergencyRoom(@RequestBody UpdateEmergencyRoomRequest request, @PathVariable UUID id) throws Exception { return _emergencyRoomService.updateEmergencyRoom(request, id); }

    @GetMapping("/{id}/emergency-room")
    public EmergencyRoomResponse getEmergencyRoom(@PathVariable UUID id) { return _emergencyRoomService.getEmergencyRoom(id); }

    @GetMapping
    public Set<EmergencyRoomResponse> getAllEmergencyRooms() throws Exception { return _emergencyRoomService.getAllEmergencyRooms(); }

    @GetMapping("/{id}/clinic")
    public Set<EmergencyRoomResponse> getAllEmergencyRoomsOfClinic(@PathVariable UUID id) throws Exception { return _emergencyRoomService.getAllEmergencyRoomsOfClinic(id); }

    @DeleteMapping("/{id}/emergency-room")
    public void deleteEmergencyRoom(@PathVariable UUID id) throws Exception { _emergencyRoomService.deleteEmergencyRoom(id); }

    @GetMapping("/search/{id}/clinic")
    public Set<EmergencyRoomResponse> getAllEmergencyRoomsOfClinicByNameAndNumber(SearchEmergencyRoomsRequest request, @PathVariable UUID id) throws Exception { return _emergencyRoomService.getAllEmergencyRoomsOfClinic(request, id); }

}
