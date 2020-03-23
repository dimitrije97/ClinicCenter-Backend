package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreatePatientRequest;
import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IPatientService;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.RequestType;
import com.example.demo.util.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService implements IPatientService {

    private final IPatientRepository _patientRepository;

    private final IUserService _userService;

    private final IUserRepository _userRepository;

    public PatientService(IPatientRepository patientRepository, IUserService userService, IUserRepository userRepository) {
        _patientRepository = patientRepository;
        _userService = userService;
        _userRepository = userRepository;
    }

    @Override
    public PatientResponse createPatient(CreatePatientRequest request) throws Exception{
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
        userRequest.setUserType(UserType.PATIENT);
        UserResponse userResponse = _userService.createUser(userRequest);
        User user = _userRepository.findOneById(userResponse.getId());
        user.setId(userResponse.getId());

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setRequestType(RequestType.PENDING);

        Patient savedPatient = _patientRepository.save(patient);

        return mapPatientToPatientResponse(savedPatient);
    }

    @Override
    public PatientResponse getPatient(UUID id) { return mapPatientToPatientResponse(_patientRepository.findOneById(id)); }

    @Override
    public PatientResponse updatePatient(UpdatePatientRequest request, UUID id) {
        Patient patient = _patientRepository.findOneById(id);
        User user = patient.getUser();
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        Patient savedPatient = _patientRepository.save(patient);

        return mapPatientToPatientResponse(savedPatient);
    }

    @Override
    public void deletePatient(UUID id) {
        Patient patient = _patientRepository.findOneById(id);
        User user = patient.getUser();
        user.setDeleted(true);
        _userRepository.save(user);
    }

    @Override
    public Set<PatientResponse> getAllPatients() {
        Set<Patient> patients = _patientRepository.findAllByRequestTypeAndUser_Deleted(RequestType.APPROVED, false);

        return patients.stream().map(patient -> mapPatientToPatientResponse(patient))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PatientResponse> getAllPendingRequests() {
        Set<Patient> patients = _patientRepository.findAllByRequestType(RequestType.PENDING);

        return patients.stream().map(patient -> mapPatientToPatientResponse(patient))
                .collect(Collectors.toSet());
    }

    private PatientResponse mapPatientToPatientResponse(Patient patient) {
        PatientResponse patientResponse = new PatientResponse();
        User user = patient.getUser();
        patientResponse.setEmail(user.getEmail());
        patientResponse.setId(patient.getId());
        patientResponse.setAddress(user.getAddress());
        patientResponse.setCity(user.getCity());
        patientResponse.setCountry(user.getCountry());
        patientResponse.setFirstName(user.getFirstName());
        patientResponse.setLastName(user.getLastName());
        patientResponse.setPhone(user.getPhone());
        patientResponse.setSsn(user.getSsn());
        return patientResponse;
    }
}
