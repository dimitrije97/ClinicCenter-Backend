package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateNurseRequest;
import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateNurseRequest;
import com.example.demo.dto.response.NurseResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Clinic;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.User;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.repository.INurseRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.INurseService;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NurseService implements INurseService {

    private final INurseRepository _nurseRepository;

    private final IUserRepository _userRepository;

    private final IUserService _userService;

    private final IClinicRepository _clinicRepository;

    public NurseService(INurseRepository nurseRepository, IUserRepository userRepository, IUserService userService, IClinicRepository clinicRepository) {
        _nurseRepository = nurseRepository;
        _userRepository = userRepository;
        _userService = userService;
        _clinicRepository = clinicRepository;
    }

    @Override
    public NurseResponse createNurse(CreateNurseRequest request, UUID clinicId) throws Exception {

        String startAtString = request.getStartAt().toString();
        String[] startAtTokens = startAtString.split(":");

        String endAtString = request.getEndAt().toString();
        String[] endAtTokens = endAtString.split(":");

        if(Integer.parseInt(startAtTokens[0]) >= Integer.parseInt(endAtTokens[0])){
            throw new Exception("Niste dobro uneli vreme početka i kraja radnog vremena medicinske sestre.");
        }

        if(Integer.parseInt(endAtTokens[0]) - Integer.parseInt(startAtTokens[0]) < 4){
            throw new Exception("Radno vreme najmanje mora biti 4h.");
        }

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword("nurse");
        userRequest.setRePassword("nurse");
        userRequest.setAddress(request.getAddress());
        userRequest.setCity(request.getCity());
        userRequest.setEmail(request.getEmail());
        userRequest.setCountry(request.getCountry());
        userRequest.setFirstName(request.getFirstName());
        userRequest.setLastName(request.getLastName());
        userRequest.setSsn(request.getSsn());
        userRequest.setPhone(request.getPhone());
        userRequest.setUserType(UserType.NURSE);
        UserResponse userResponse = _userService.createUser(userRequest);
        User user = _userRepository.findOneById(userResponse.getId());
        user.setId(userResponse.getId());

        Nurse nurse = new Nurse();
        nurse.setUser(user);
        nurse.setStartAt(request.getStartAt());
        nurse.setEndAt(request.getEndAt());

        Clinic clinic = _clinicRepository.findOneById(clinicId);
        nurse.setClinic(clinic);

        Nurse savedNurse = _nurseRepository.save(nurse);

        return mapNurseToNurseResponse(savedNurse);
    }

    @Override
    public NurseResponse updateNurse(UpdateNurseRequest request, UUID  id) throws Exception {
        Nurse nurse = _nurseRepository.findOneById(id);
        User user = nurse.getUser();
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        Nurse savedNurse = _nurseRepository.save(nurse);

        return mapNurseToNurseResponse(savedNurse);
    }

    @Override
    public NurseResponse getNurse(UUID id) { return mapNurseToNurseResponse(_nurseRepository.findOneById(id)); }

    @Override
    public Set<NurseResponse> getAllNurses() throws Exception {
        Set<Nurse> nurses = _nurseRepository.findAllByUser_Deleted(false);

        if(nurses.isEmpty()){
            throw new Exception("Ne postoji nijeda medicinska sestra.");
        }

        return nurses.stream().map(nurse -> mapNurseToNurseResponse(nurse))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<NurseResponse> getAllNursesOfClinic(UUID clinicId) throws Exception {
        Set<Nurse> nurses = _nurseRepository.findAllByUser_DeletedAndClinic_Id(false, clinicId);

        if(nurses.isEmpty()){
            throw new Exception("Ne postoji nijeda medicinska sestra u klinici.");
        }

        return nurses.stream().map(nurse -> mapNurseToNurseResponse(nurse))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteNurse(UUID id) {
        Nurse nurse = _nurseRepository.findOneById(id);
        User user = nurse.getUser();
        user.setDeleted(true);
        _nurseRepository.save(nurse);
    }

    public NurseResponse mapNurseToNurseResponse(Nurse nurse) {
        NurseResponse nurseResponse = new NurseResponse();
        User user = nurse.getUser();
        nurseResponse.setEmail(user.getEmail());
        nurseResponse.setId(nurse.getId());
        nurseResponse.setAddress(user.getAddress());
        nurseResponse.setCity(user.getCity());
        nurseResponse.setCountry(user.getCountry());
        nurseResponse.setFirstName(user.getFirstName());
        nurseResponse.setLastName(user.getLastName());
        nurseResponse.setPhone(user.getPhone());
        nurseResponse.setStartAt(nurse.getStartAt());
        nurseResponse.setEndAt(nurse.getEndAt());

        return nurseResponse;
    }
}
