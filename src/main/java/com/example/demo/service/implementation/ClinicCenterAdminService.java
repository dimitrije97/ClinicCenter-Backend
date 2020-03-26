package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateClinicCenterAdminRequest;
import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateClinicCenterAdminRequest;
import com.example.demo.dto.response.ClinicCenterAdminResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.ClinicCenterAdmin;
import com.example.demo.entity.User;
import com.example.demo.repository.IClinicCenterAdminRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IClinicCenterAdminService;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.ClinicCenterAdminType;
import com.example.demo.util.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClinicCenterAdminService implements IClinicCenterAdminService {

    private final IClinicCenterAdminRepository _clinicCenterAdminRepository;

    private final IUserService _userService;

    private final IUserRepository _userRepository;

    public ClinicCenterAdminService(IClinicCenterAdminRepository clinicCenterAdminRepository, IUserService userService, IUserRepository userRepository) {
        _clinicCenterAdminRepository = clinicCenterAdminRepository;
        _userService = userService;
        _userRepository = userRepository;
    }

    @Override
    public ClinicCenterAdminResponse createClinicCenterAdmin(CreateClinicCenterAdminRequest request) throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword(request.getPassword());
        userRequest.setRePassword(request.getRePassword());
        userRequest.setAddress(request.getAddress());
        userRequest.setCity(request.getCity());
        userRequest.setEmail(request.getEmail());
        userRequest.setCountry(request.getCountry());
        userRequest.setFirstName(request.getFirstName());
        userRequest.setLastName(request.getLastName());
        userRequest.setSsn(request.getSsn());
        userRequest.setPhone(request.getPhone());
        userRequest.setUserType(UserType.CLINIC_CENTER_ADMIN);
        UserResponse userResponse = _userService.createUser(userRequest);
        User user = _userRepository.findOneById(userResponse.getId());
        user.setId(userResponse.getId());

        ClinicCenterAdmin clinicCenterAdmin = new ClinicCenterAdmin();
        clinicCenterAdmin.setUser(user);
        clinicCenterAdmin.setClinicCenterAdminType(ClinicCenterAdminType.REGULAR);


        ClinicCenterAdmin savedClinicCenterAdmin = _clinicCenterAdminRepository.save(clinicCenterAdmin);

        return mapClinicCenterAdminToClinicCenterAdminResponse(savedClinicCenterAdmin);
    }

    @Override
    public ClinicCenterAdminResponse updateClinicCenterAdmin(UpdateClinicCenterAdminRequest request, UUID id) throws Exception {
        ClinicCenterAdmin clinicCenterAdmin = _clinicCenterAdminRepository.findOneById(id);
        User user = clinicCenterAdmin.getUser();

        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        ClinicCenterAdmin savedClinicCenterAdmin = _clinicCenterAdminRepository.save(clinicCenterAdmin);

        return mapClinicCenterAdminToClinicCenterAdminResponse(savedClinicCenterAdmin);
    }

    @Override
    public ClinicCenterAdminResponse getClinicCenterAdmin(UUID id) {
        return mapClinicCenterAdminToClinicCenterAdminResponse(_clinicCenterAdminRepository.findOneById(id));
    }

    @Override
    public Set<ClinicCenterAdminResponse> getAllClinincCenterAdmins() {
        Set<ClinicCenterAdmin> clinicCenterAdmins = _clinicCenterAdminRepository.findAllByUser_Deleted(false);

        return clinicCenterAdmins.stream().map(clinicCenterAdmin -> mapClinicCenterAdminToClinicCenterAdminResponse(clinicCenterAdmin))
                .collect(Collectors.toSet());
    }

    private ClinicCenterAdminResponse mapClinicCenterAdminToClinicCenterAdminResponse(ClinicCenterAdmin clinicCenterAdmin) {
        ClinicCenterAdminResponse clinicCenterAdminResponse = new ClinicCenterAdminResponse();
        User user = clinicCenterAdmin.getUser();
        clinicCenterAdminResponse.setEmail(user.getEmail());
        clinicCenterAdminResponse.setId(clinicCenterAdmin.getId());
        clinicCenterAdminResponse.setAddress(user.getAddress());
        clinicCenterAdminResponse.setCity(user.getCity());
        clinicCenterAdminResponse.setCountry(user.getCountry());
        clinicCenterAdminResponse.setFirstName(user.getFirstName());
        clinicCenterAdminResponse.setLastName(user.getLastName());
        clinicCenterAdminResponse.setPhone(user.getPhone());
        clinicCenterAdminResponse.setSsn(user.getSsn());
        clinicCenterAdminResponse.setClinicCenterAdminType(clinicCenterAdmin.getClinicCenterAdminType());
        return clinicCenterAdminResponse;
    }
}
