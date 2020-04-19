package com.example.demo.service.implementation;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.IEmailService;
import com.example.demo.service.IPatientService;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import com.example.demo.util.enums.RequestType;
import com.example.demo.util.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    private final IClinicRepository _clinicRepository;

    private final IEmailService _emailService;
    
    private final IMedicalRecordReposiroty _medicalRecordReposiroty;

    public PatientService(IPatientRepository patientRepository, IUserService userService, IUserRepository userRepository, IScheduleRepository scheduleRepository, IClinicRepository clinicRepository, IEmailService emailService, IMedicalRecordReposiroty medicalRecordReposiroty) {
        _patientRepository = patientRepository;
        _userService = userService;
        _userRepository = userRepository;
        _scheduleRepository = scheduleRepository;
        _clinicRepository = clinicRepository;
        _emailService = emailService;
        _medicalRecordReposiroty = medicalRecordReposiroty;
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
    public Set<PatientResponse> getAllPatients() throws Exception {
        Set<Patient> patients = _patientRepository.findAllByRequestTypeAndUser_Deleted(RequestType.APPROVED, false);

        if(patients.isEmpty()){
            throw new Exception("Ne postoji nijedan pacijent.");
        }

        return patients.stream().map(patient -> mapPatientToPatientResponse(patient))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PatientResponse> getAllPendingRequests() throws Exception{
        Set<Patient> patients = _patientRepository.findAllByRequestType(RequestType.PENDING);

        if(patients.isEmpty()){
            throw new Exception("Nemate nijedan novi zahtev za registraciju.");
        }

        return patients.stream().map(patient -> mapPatientToPatientResponse(patient))
                .collect(Collectors.toSet());
    }

    @Override
    public PatientResponse confirmRegistrationRequest(ApprovePatientRequest request) {
        Patient patient = _patientRepository.findOneById(request.getPatientId());
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
    public PatientResponse approveRegistration(ApprovePatientRequest request) throws Exception {
        Patient patient = _patientRepository.findOneById(request.getPatientId());
        if(patient.getRequestType().equals(RequestType.APPROVED)){
            throw new Exception("Već ste potvrdili Vašu registraciju.");
        }else if(patient.getRequestType().equals(RequestType.CONFIRMING)){
            patient.setRequestType(RequestType.APPROVED);
            Patient savedPatient = _patientRepository.save(patient);
            return mapPatientToPatientResponse(savedPatient);
        }
        throw new Exception("Vaš nalog je obrisan.");
    }

    @Override
    public Set<PatientResponse> getAllPatientsByClinic(UUID clinicId) {
        Set<Patient> allPatients = _patientRepository.findAllByRequestTypeAndUser_Deleted(RequestType.APPROVED, false);
        Set<Patient> patients = new HashSet<>();
        for (Patient patient: allPatients) {
            for (Doctor doctor: patient.getDoctors()) {
                if(doctor.getClinic() == _clinicRepository.findOneById(clinicId)){
                    patients.add(patient);
                    break;
                }
            }
        }

        return patients.stream().map(patient -> mapPatientToPatientResponse(patient))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PatientResponse> getAllPatientsWithoutMedicalRecord() throws Exception {
        Set<Patient> patients = _patientRepository.findAllByRequestTypeAndUser_Deleted(RequestType.APPROVED, false);
        Set<Patient> patientsWithoutMedicalRecord = new HashSet<>();
        List<MedicalRecord> records = _medicalRecordReposiroty.findAll();
        for (Patient p: patients) {
            boolean flag = true;
            for(MedicalRecord mr: records){
                if(p.getId().equals(mr.getPatient().getId())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                patientsWithoutMedicalRecord.add(p);
            }
        }
        if(patientsWithoutMedicalRecord.isEmpty()){
            throw new Exception("Ne postoji nijedan pacijent bez zdravstvenog kartona.");
        }
        return patientsWithoutMedicalRecord.stream()
                .map(patient -> mapPatientToPatientResponse(patient))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PatientResponse> getAllPatientsWithMedicalRecord() throws Exception {
        Set<Patient> patients = _patientRepository.findAllByRequestTypeAndUser_Deleted(RequestType.APPROVED, false);
        Set<Patient> patientsWithtMedicalRecord = new HashSet<>();
        List<MedicalRecord> records = _medicalRecordReposiroty.findAll();
        for (Patient p: patients) {
            boolean flag = false;
            for(MedicalRecord mr: records){
                if(p.getId().equals(mr.getPatient().getId())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                patientsWithtMedicalRecord.add(p);
            }
        }
        if(patientsWithtMedicalRecord.isEmpty()){
            throw new Exception("Ne postoji nijedan pacijent sa zdravstvenim kartonom.");
        }
        return patientsWithtMedicalRecord.stream()
                .map(patient -> mapPatientToPatientResponse(patient))
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
