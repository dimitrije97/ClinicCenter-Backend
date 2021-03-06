package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateEmergencyRoomRequest;
import com.example.demo.dto.request.SearchEmergencyRoomsRequest;
import com.example.demo.dto.request.UpdateEmergencyRoomRequest;
import com.example.demo.dto.response.EmergencyRoomResponse;
import com.example.demo.entity.Clinic;
import com.example.demo.entity.EmergencyRoom;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.repository.IEmergencyRoomRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IEmergencyRoomService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmergencyRoomService implements IEmergencyRoomService {

    private final IEmergencyRoomRepository _emergencyRoomRepository;

    private final IClinicRepository _clinicRepository;

    private final IScheduleRepository _scheduleRepository;

    public EmergencyRoomService(IEmergencyRoomRepository emergencyRoomRepository, IClinicRepository clinicRepository, IScheduleRepository scheduleRepository) {
        _emergencyRoomRepository = emergencyRoomRepository;
        _clinicRepository = clinicRepository;
        _scheduleRepository = scheduleRepository;
    }

    @Override
    public EmergencyRoomResponse createEmergencyRoom(CreateEmergencyRoomRequest request, UUID id) throws Exception {
        Set<EmergencyRoom> emergencyRooms = _emergencyRoomRepository.findAllByDeleted(false);
        for (EmergencyRoom er: emergencyRooms){
            if(er.getNumber().equals(request.getNumber())){
                throw new Exception("Već postoji sala sa istim brojem.");
            }
        }
        EmergencyRoom emergencyRoom = new EmergencyRoom();
        emergencyRoom.setDeleted(false);
        emergencyRoom.setName(request.getName());
        emergencyRoom.setNumber(request.getNumber());
        Clinic clinic = _clinicRepository.findOneById(id);
        emergencyRoom.setClinic(clinic);

        EmergencyRoom savedEmergencyRoom = _emergencyRoomRepository.save(emergencyRoom);
        return mapEmergencyRoomToEmergencyRoomResponse(savedEmergencyRoom);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public EmergencyRoomResponse updateEmergencyRoom(UpdateEmergencyRoomRequest request, UUID id) throws Exception {
        Set<EmergencyRoom> emergencyRooms = _emergencyRoomRepository.findAllByDeleted(false);
//        for (EmergencyRoom er: emergencyRooms){
//            if(er.getNumber().equals(request.getNumber())){
//                throw new Exception("Već postoji sala sa istim brojem.");
//            }
//        }
        EmergencyRoom emergencyRoom = _emergencyRoomRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getExamination().getEmergencyRoom().getId().equals(id)){
                throw new Exception("Postoji zakazan pregled u sali.");
            }
        }
        emergencyRoom.setNumber(request.getNumber());
        emergencyRoom.setName(request.getName());
        EmergencyRoom savedEmergencyRoom = _emergencyRoomRepository.save(emergencyRoom);
        return mapEmergencyRoomToEmergencyRoomResponse(savedEmergencyRoom);
    }

    @Override
    public EmergencyRoomResponse getEmergencyRoom(UUID id) { return mapEmergencyRoomToEmergencyRoomResponse(_emergencyRoomRepository.findOneById(id)); }

    @Override
    public Set<EmergencyRoomResponse> getAllEmergencyRooms() throws Exception {
        Set<EmergencyRoom> emergencyRooms = _emergencyRoomRepository.findAllByDeleted(false);
        if(emergencyRooms.isEmpty()){
            throw new Exception("Ne postoji nijedna sala.");
        }
        return emergencyRooms.stream().map(emergencyRoom -> mapEmergencyRoomToEmergencyRoomResponse(emergencyRoom))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EmergencyRoomResponse> getAllEmergencyRoomsOfClinic(UUID id) throws Exception {
        Set<EmergencyRoom> emergencyRooms = _emergencyRoomRepository.findAllByDeletedAndClinic_Id(false, id);
        if(emergencyRooms.isEmpty()){
            throw new Exception("Ne postoji nijedna sala u klinici.");
        }
        return emergencyRooms.stream().map(emergencyRoom -> mapEmergencyRoomToEmergencyRoomResponse(emergencyRoom))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteEmergencyRoom(UUID id) throws Exception {
        EmergencyRoom emergencyRoom = _emergencyRoomRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getExamination().getEmergencyRoom().getId().equals(id)){
                throw new Exception("Postoji zakazan pregled u sali.");
            }
        }
        emergencyRoom.setDeleted(true);
        emergencyRoom.getClinic().getEmergencyRooms().remove(emergencyRoom);
        _emergencyRoomRepository.save(emergencyRoom);
    }

    @Override
    public Set<EmergencyRoomResponse> getAllEmergencyRoomsOfClinic(SearchEmergencyRoomsRequest request, UUID id) throws Exception {
        Set<EmergencyRoom> emergencyRooms = _emergencyRoomRepository.findAllByDeletedAndClinic_Id(false, id);

        Set<EmergencyRoom> searchedByName = new HashSet<>();
        Set<EmergencyRoom> searchedByNameAndNumber = new HashSet<>();

        for(EmergencyRoom er: emergencyRooms){
            if(er.getName().toLowerCase().contains(request.getName().toLowerCase())){
                searchedByName.add(er);
            }
        }

        for(EmergencyRoom er: searchedByName){
            if(er.getNumber().toLowerCase().contains(request.getNumber().toLowerCase())){
                searchedByNameAndNumber.add(er);
            }
        }

        return searchedByNameAndNumber.stream()
                .map(emergencyRoom -> mapEmergencyRoomToEmergencyRoomResponse(emergencyRoom))
                .collect(Collectors.toSet());
    }

    public EmergencyRoomResponse mapEmergencyRoomToEmergencyRoomResponse(EmergencyRoom emergencyRoom){
        EmergencyRoomResponse emergencyRoomResponse = new EmergencyRoomResponse();
        emergencyRoomResponse.setId(emergencyRoom.getId());
        emergencyRoomResponse.setName(emergencyRoom.getName());
        emergencyRoomResponse.setNumber(emergencyRoom.getNumber());
        return emergencyRoomResponse;
    }
}
