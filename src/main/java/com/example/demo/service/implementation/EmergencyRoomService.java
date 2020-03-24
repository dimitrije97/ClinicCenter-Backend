package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateEmergencyRoomRequest;
import com.example.demo.dto.request.UpdateEmergencyRoomRequest;
import com.example.demo.dto.response.EmergencyRoomResponse;
import com.example.demo.entity.Clinic;
import com.example.demo.entity.EmergencyRoom;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.repository.IEmergencyRoomRepository;
import com.example.demo.service.IEmergencyRoomService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmergencyRoomService implements IEmergencyRoomService {

    private final IEmergencyRoomRepository _emergencyRoomRepository;

    private final IClinicRepository _clinicRepository;

    public EmergencyRoomService(IEmergencyRoomRepository emergencyRoomRepository, IClinicRepository clinicRepository) {
        _emergencyRoomRepository = emergencyRoomRepository;
        _clinicRepository = clinicRepository;
    }

    @Override
    public EmergencyRoomResponse createEmergencyRoom(CreateEmergencyRoomRequest request, UUID id) throws Exception {
        EmergencyRoom emergencyRoom = new EmergencyRoom();
        emergencyRoom.setDeleted(false);
        emergencyRoom.setName(request.getName());
        emergencyRoom.setNumber(request.getNumber());
        Clinic clinic = _clinicRepository.findOneById(id);
        emergencyRoom.setClinic(clinic);

        EmergencyRoom savedEmergencyRoom = _emergencyRoomRepository.save(emergencyRoom);
        return mapEmergencyRoomToEmergencyRoomResponse(savedEmergencyRoom);
    }

    @Override
    public EmergencyRoomResponse updateEmergencyRoom(UpdateEmergencyRoomRequest request, UUID id) throws Exception {
        EmergencyRoom emergencyRoom = _emergencyRoomRepository.findOneById(id);
        emergencyRoom.setNumber(request.getNumber());
        emergencyRoom.setName(request.getName());
        EmergencyRoom savedEmergencyRoom = _emergencyRoomRepository.save(emergencyRoom);
        return mapEmergencyRoomToEmergencyRoomResponse(savedEmergencyRoom);
    }

    @Override
    public EmergencyRoomResponse getEmergencyRoom(UUID id) { return mapEmergencyRoomToEmergencyRoomResponse(_emergencyRoomRepository.findOneById(id)); }

    @Override
    public Set<EmergencyRoomResponse> getAllEmergencyRooms() {
        Set<EmergencyRoom> emergencyRooms = _emergencyRoomRepository.findAllByDeleted(false);
        return emergencyRooms.stream().map(emergencyRoom -> mapEmergencyRoomToEmergencyRoomResponse(emergencyRoom))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EmergencyRoomResponse> getAllEmergencyRoomsOfClinic(UUID id) {
        Set<EmergencyRoom> emergencyRooms = _emergencyRoomRepository.findAllByDeletedAndClinic_Id(false, id);
        return emergencyRooms.stream().map(emergencyRoom -> mapEmergencyRoomToEmergencyRoomResponse(emergencyRoom))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteEmergencyRoom(UUID id) {
        EmergencyRoom emergencyRoom = _emergencyRoomRepository.findOneById(id);
        emergencyRoom.setDeleted(true);
        _emergencyRoomRepository.save(emergencyRoom);
    }

    public EmergencyRoomResponse mapEmergencyRoomToEmergencyRoomResponse(EmergencyRoom emergencyRoom){
        EmergencyRoomResponse emergencyRoomResponse = new EmergencyRoomResponse();
        emergencyRoomResponse.setId(emergencyRoom.getId());
        emergencyRoomResponse.setName(emergencyRoom.getName());
        emergencyRoomResponse.setNumber(emergencyRoom.getNumber());
        return emergencyRoomResponse;
    }
}