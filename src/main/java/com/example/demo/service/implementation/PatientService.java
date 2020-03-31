package com.example.demo.service.implementation;

import com.example.demo.dto.request.AdminsMessageAboutDenyingRegistrationRequest;
import com.example.demo.dto.request.CreatePatientRequest;
import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.User;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IEmailService;
import com.example.demo.service.IPatientService;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import com.example.demo.util.enums.RequestType;
import com.example.demo.util.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService implements IPatientService {

    private final IPatientRepository _patientRepository;

    private final IUserService _userService;

    private final IUserRepository _userRepository;

    private final IScheduleRepository _scheduleRepository;

    private final IEmailService _emailService;

    public PatientService(IPatientRepository patientRepository, IUserService userService, IUserRepository userRepository, IScheduleRepository scheduleRepository, IEmailService emailService) {
        _patientRepository = patientRepository;
        _userService = userService;
        _userRepository = userRepository;
        _scheduleRepository = scheduleRepository;
        _emailService = emailService;
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
    public void deletePatient(UUID id) throws Exception {
        Patient patient = _patientRepository.findOneById(id);
        User user = patient.getUser();
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getPatient().getId().equals(id)){
                flag = true;
                break;
            }
        }
        if(flag){
            throw new Exception("Pacijent poseduje zakazan pregled.");
        }
        patient.getUser().setDeleted(true);
        _patientRepository.save(patient);
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

    @Override
    public PatientResponse confirmRegistrationRequest(UUID patientId) {
        Patient patient = _patientRepository.findOneById(patientId);
        patient.setRequestType(RequestType.CONFIRMING);
        Patient savedPatient = _patientRepository.save(patient);

        _emailService.approveRegistrationMail(savedPatient);

        return mapPatientToPatientResponse(savedPatient);
    }

    @Override
    public void denyRegistrationRequest(UUID patientId, AdminsMessageAboutDenyingRegistrationRequest request) {
        Patient patient = _patientRepository.findOneById(patientId);
        patient.setRequestType(RequestType.DENIED);
        Patient savedPatient = _patientRepository.save(patient);

        _emailService.denyRegistrationMail(savedPatient, request.getMessage());
    }

    @Override
    public PatientResponse approveRegistration(UUID patientId) throws Exception {
        Patient patient = _patientRepository.findOneById(patientId);
        if(patient.getRequestType().equals(RequestType.APPROVED)){
            throw new Exception("Vec ste potvrdili Vasu registraciju.");
        }else if(patient.getRequestType().equals(RequestType.CONFIRMING)){
            patient.setRequestType(RequestType.APPROVED);
            Patient savedPatient = _patientRepository.save(patient);
            return mapPatientToPatientResponse(savedPatient);
        }
        throw new Exception("Vas nalog je obrisan.");
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
