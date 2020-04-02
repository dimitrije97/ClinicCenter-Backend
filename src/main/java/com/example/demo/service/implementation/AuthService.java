package com.example.demo.service.implementation;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.NewPasswordRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.IAuthService;
import com.example.demo.service.IClinicService;
import com.example.demo.util.enums.RequestType;
import com.example.demo.util.enums.UserType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    private final PasswordEncoder _passwordEncoder;

    private final IUserRepository _userRepository;

    private final IPatientRepository _patientRepository;

    private final IAdminRepository _adminRepository;

    private final IDoctorRepository _doctorRepository;

    private final INurseRepository _nurseRepository;

    private final IClinicCenterAdminRepository _clinicCenterAdminRepository;

    private final IClinicService _clinicService;

    public AuthService(PasswordEncoder passwordEncoder, IUserRepository userRepository,
                       IPatientRepository patientRepository, IAdminRepository adminRepository, IDoctorRepository doctorRepository, INurseRepository nurseRepository, IClinicCenterAdminRepository clinicCenterAdminRepository, IClinicService clinicService) {
        _passwordEncoder = passwordEncoder;
        _userRepository = userRepository;
        _patientRepository = patientRepository;
        _adminRepository = adminRepository;
        _doctorRepository = doctorRepository;
        _nurseRepository = nurseRepository;
        _clinicCenterAdminRepository = clinicCenterAdminRepository;
        _clinicService = clinicService;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        User user = _userRepository.findOneByEmail(request.getUsername());

        if (user == null) {
            throw new Exception(String.format("Ne postoji korisnik sa datim emailom."));
        }

        if (!_passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Pogresna lozinka.");
        }

        if(user.isDeleted()){
            throw new Exception("Vas nalog je obrisan.");
        }

        if(user.getUserType().equals(UserType.PATIENT)){
            Patient patient = _patientRepository.findOneByUser_Id(user.getId());
            if(patient.getRequestType().equals(RequestType.PENDING)){
                throw new Exception("Vas nalog i dalje nije prihvacen od strane administratora klinickog centra.");
            }else if(patient.getRequestType().equals(RequestType.DENIED)){
                throw new Exception("Vas nalog je odbijen od strane administratora klinickog centra.");
            }else if(patient.getRequestType().equals(RequestType.CONFIRMING)){
                throw new Exception("Prihvatite aktivaciju vaseg naloga klikom na link koji ste dobili u emailu.");
            }

            if(user.getFirstTimeLoggedIn() == null){
                user.setFirstTimeLoggedIn(new Date());
                _userRepository.save(user);
            }
        }

        UserResponse userResponse = mapUserToUserResponse(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserResponse(userResponse);

        return loginResponse;
    }

    @Override
    public LoginResponse setNewPassword(UUID id, NewPasswordRequest request) throws Exception {
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new Exception("Lozinke koje ste uneli se ne podudaraju!");
        }

        Admin admin = _adminRepository.findOneById(id);
        Doctor doctor = _doctorRepository.findOneById(id);
        Nurse nurse = _nurseRepository.findOneById(id);
        ClinicCenterAdmin clinicCenterAdmin = _clinicCenterAdminRepository.findOneById(id);

        User user = null;

        if(admin != null){
            user = admin.getUser();
        }else if(doctor != null){
            user = doctor.getUser();
        }else if(nurse != null){
            user = nurse.getUser();
        }else if(clinicCenterAdmin != null){
            user = clinicCenterAdmin.getUser();
        }

        user.setPassword(_passwordEncoder.encode(request.getPassword()));

        if(user.getFirstTimeLoggedIn() == null){
            user.setFirstTimeLoggedIn(new Date());
        }

        if(admin != null){
            _adminRepository.save(admin);
        }else if(doctor != null){
            _doctorRepository.save(doctor);
        }else if(nurse != null){
            _nurseRepository.save(nurse);
        }else if(clinicCenterAdmin != null){
            _clinicCenterAdminRepository.save(clinicCenterAdmin);
        }

        UserResponse userResponse = mapUserToUserResponse(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserResponse(userResponse);

        return loginResponse;
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        UUID id = null;
        UUID clinicId = null;
        if (user.getUserType().equals(UserType.PATIENT)) {
            id = user.getPatient().getId();
        }else if(user.getUserType().equals(UserType.ADMIN)){
            id = user.getAdmin().getId();
            clinicId = user.getAdmin().getClinic().getId();
        }else if(user.getUserType().equals(UserType.CLINIC_CENTER_ADMIN)){
            id = user.getClinicCenterAdmin().getId();
        }else if(user.getUserType().equals(UserType.DOCTOR)){
            id = user.getDoctor().getId();
            clinicId = user.getDoctor().getClinic().getId();
        }else if(user.getUserType().equals(UserType.NURSE)){
            id = user.getNurse().getId();
            clinicId = user.getNurse().getClinic().getId();
        }
        userResponse.setId(id);
        userResponse.setAddress(user.getAddress());
        userResponse.setCity(user.getCity());
        userResponse.setCountry(user.getCountry());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setSsn(user.getSsn());
        userResponse.setUserType(user.getUserType());

        // only on login
        userResponse.setSetNewPassword(user.getFirstTimeLoggedIn() == null);

        if(user.getUserType().equals(UserType.ADMIN) || user.getUserType().equals(UserType.DOCTOR) || user.getUserType().equals(UserType.NURSE)) {
            ClinicResponse clinicResponse = _clinicService.getClinic(clinicId);
            userResponse.setMyClinic(clinicResponse);
        }

        return userResponse;
    }
}
