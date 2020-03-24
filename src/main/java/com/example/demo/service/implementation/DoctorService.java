package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateDoctorRequest;
import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateDoctorRequest;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Clinic;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.ExaminationType;
import com.example.demo.entity.User;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.repository.IExaminationTypeRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IDoctorService;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorService implements IDoctorService {

    private final IUserService _userService;

    private final IUserRepository _userRepository;

    private final IDoctorRepository _doctorRepository;

    private final IClinicRepository _clinicRepository;

    private final IExaminationTypeRepository _examinationTypeRepository;

    public DoctorService(IUserService userService, IUserRepository userRepository, IDoctorRepository doctorRepository, IClinicRepository clinicRepository, IExaminationTypeRepository examinationTypeRepository) {
        _userService = userService;
        _userRepository = userRepository;
        _doctorRepository = doctorRepository;
        _clinicRepository = clinicRepository;
        _examinationTypeRepository = examinationTypeRepository;
    }

    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest doctorRequest, UUID clinicId) throws Exception {

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword(doctorRequest.getPassword());
        userRequest.setRePassword(doctorRequest.getRePassword());
        userRequest.setAddress(doctorRequest.getAddress());
        userRequest.setCity(doctorRequest.getCity());
        userRequest.setEmail(doctorRequest.getEmail());
        userRequest.setCountry(doctorRequest.getCountry());
        userRequest.setFirstName(doctorRequest.getFirstName());
        userRequest.setLastName(doctorRequest.getLastName());
        userRequest.setSsn(doctorRequest.getSsn());
        userRequest.setPhone(doctorRequest.getPhone());
        userRequest.setUserType(UserType.DOCTOR);
        UserResponse userResponse = _userService.createUser(userRequest);
        User user = _userRepository.findOneById(userResponse.getId());
        user.setId(userResponse.getId());

        Doctor doctor = new Doctor();
        doctor.setUser(user);

        Clinic clinic = _clinicRepository.findOneById(clinicId);
        doctor.setClinic(clinic);

        doctor.setExaminationType(_examinationTypeRepository.findOneById(doctorRequest.getExaminationTypeId()));

        Doctor savedDoctor = _doctorRepository.save(doctor);

        return mapDoctorToDoctorResponse(savedDoctor);
    }

    @Override
    public DoctorResponse updateDoctor(UpdateDoctorRequest updateDoctorRequest, UUID id) throws Exception {

        Doctor doctor = _doctorRepository.findOneById(id);
        User user = doctor.getUser();

        user.setAddress(updateDoctorRequest.getAddress());
        user.setCity(updateDoctorRequest.getCity());
        user.setCountry(updateDoctorRequest.getCountry());
        user.setFirstName(updateDoctorRequest.getFirstName());
        user.setLastName(updateDoctorRequest.getLastName());
        user.setPhone(updateDoctorRequest.getPhone());

        Doctor savedDoctor = _doctorRepository.save(doctor);

        return mapDoctorToDoctorResponse(savedDoctor);
    }

    @Override
    public void deleteDoctor(UUID id) {

        Doctor doctor = _doctorRepository.findOneById(id);
        doctor.getUser().setDeleted(true);
        _doctorRepository.save(doctor);
    }

    @Override
    public DoctorResponse getDoctor(UUID id) { return mapDoctorToDoctorResponse(_doctorRepository.findOneById(id)); }

    @Override
    public Set<DoctorResponse> getAllDoctors() {

        Set<Doctor> doctors = _doctorRepository.findAllByUser_Deleted(false);

        return doctors.stream().map(doctor -> mapDoctorToDoctorResponse(doctor))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DoctorResponse> getAllDoctorsOfClinic(UUID clinicId) {

        Set<Doctor> doctors = _doctorRepository.findAllByClinic_IdAndUser_Deleted(clinicId, false);

        return doctors.stream().map(doctor -> mapDoctorToDoctorResponse(doctor))
                .collect(Collectors.toSet());
    }

    private DoctorResponse mapDoctorToDoctorResponse(Doctor doctor) {
        DoctorResponse doctorResponse = new DoctorResponse();
        User user = doctor.getUser();
        doctorResponse.setEmail(user.getEmail());
        doctorResponse.setId(doctor.getId());
        doctorResponse.setAddress(user.getAddress());
        doctorResponse.setCity(user.getCity());
        doctorResponse.setCountry(user.getCountry());
        doctorResponse.setFirstName(user.getFirstName());
        doctorResponse.setLastName(user.getLastName());
        doctorResponse.setPhone(user.getPhone());
        doctorResponse.setSsn(user.getSsn());
        doctorResponse.setExaminationTypeId(doctor.getExaminationType().getId());
        return doctorResponse;
    }
}