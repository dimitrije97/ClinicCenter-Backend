package com.example.demo.service.implementation;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.IExaminationService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import com.example.demo.util.enums.RequestType;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExaminationService implements IExaminationService {

    private final IExaminationRepository _examinationRepository;

    private final IExaminationTypeRepository _examinationTypeRepository;

    private final IEmergencyRoomRepository _emergencyRoomRepository;

    private final IDoctorRepository _doctorRepository;

    private final IPatientRepository _patientRepository;

    private final IScheduleRepository _scheduleRepository;

    private final IClinicRepository _clinicRepository;

    public ExaminationService(IExaminationRepository examinationRepository, IExaminationTypeRepository examinationTypeRepository, IEmergencyRoomRepository emergencyRoomRepository, IDoctorRepository doctorRepository, IPatientRepository patientRepository, IScheduleRepository scheduleRepository, IClinicRepository clinicRepository) {
        _examinationRepository = examinationRepository;
        _examinationTypeRepository = examinationTypeRepository;
        _emergencyRoomRepository = emergencyRoomRepository;
        _doctorRepository = doctorRepository;
        _patientRepository = patientRepository;
        _scheduleRepository = scheduleRepository;
        _clinicRepository = clinicRepository;
    }

    @Override
    public ExaminationResponse createExaminationRequestByPatient(CreateExaminationRequestByPatient request) {

        Examination examination = new Examination();
        Schedule schedule = new Schedule();
        Patient patient = _patientRepository.findOneById(request.getPatientId());
        Doctor doctor = _doctorRepository.findOneById(request.getDoctorId());
        schedule.setDate(request.getDate());
        schedule.setStartAt(request.getStartAt());
        schedule.setEndAt(request.getStartAt().plusHours(1L));
        schedule.setDoctor(doctor);
        schedule.setPatient(patient);
        examination.setStatus(RequestType.PENDING);
        schedule.setExamination(examination);
        schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_EXAMINATION);
        schedule.setApproved(false);
        Schedule savedSchedule = _scheduleRepository.save(schedule);
        examination.setSchedule(savedSchedule);

        Examination savedExamination = _examinationRepository.save(examination);

        return mapExaminationToExaminationResponse(savedExamination, savedSchedule);
    }

    @Override
    public ExaminationResponse confirmExaminationRequestByAdmin(CreateExaminationRequestByAdmin request) {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        examination.setEmergencyRoom(_emergencyRoomRepository.findOneById(request.getEmergencyRoomId()));
        examination.setStatus(RequestType.CONFIRMING);
        Examination savedExamination = _examinationRepository.save(examination);

        return mapExaminationToExaminationResponse(savedExamination, savedExamination.getSchedule());
    }

    @Override
    public ExaminationResponse approveExamination(ApproveExaminationRequest request) {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        examination.setStatus(RequestType.APPROVED);
        examination.getSchedule().setReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        examination.getSchedule().setApproved(true);
        examination.getSchedule().getPatient().getDoctors().add(examination.getSchedule().getDoctor());
        examination.getSchedule().getDoctor().getPatients().add(examination.getSchedule().getPatient());
        examination.getSchedule().getDoctor().getSchedules().add(examination.getSchedule());
        Examination savedExamination = _examinationRepository.save(examination);

        return mapExaminationToExaminationResponse(savedExamination, savedExamination.getSchedule());
    }

    @Override
    public void denyExaminationRequest(ApproveExaminationRequest request) {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        examination.setStatus(RequestType.DENIED);
        _examinationRepository.save(examination);
    }

    @Override
    public ExaminationResponse createPotentialExamination(CreatePotentialExaminationRequest request) throws Exception {
        Examination examination = new Examination();
        Schedule schedule = new Schedule();
        List<Schedule> schedules = _scheduleRepository.findAllByApproved(true);
        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getId().equals(request.getDoctorId())){
                if(schedules.get(i).getDate().getYear() == request.getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == request.getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == request.getDate().getDay()) {
                    if (!(request.getStartAt().isBefore(schedules.get(i).getStartAt()) && request.getStartAt().plusHours(1L).isAfter(schedules.get(i).getEndAt()))) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            throw new Exception("Ne mozete rezervisati pregled u ovom terminu, doktor je tada zauzet.");
        }
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getExamination().getEmergencyRoom().getId().equals(request.getEmergencyRoomId())){
                if(schedules.get(i).getDate().getYear() == request.getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == request.getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == request.getDate().getDay()) {
                    if (!(request.getStartAt().isBefore(schedules.get(i).getStartAt()) && request.getStartAt().plusHours(1L).isAfter(schedules.get(i).getEndAt()))) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            throw new Exception("Ne mozete rezervisati pregled u ovom terminu u ovoj sali.");
        }
        schedule.setApproved(false);
        schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_EXAMINATION);
        schedule.setDoctor(_doctorRepository.findOneById(request.getDoctorId()));
        schedule.setDate(request.getDate());
        schedule.setStartAt(request.getStartAt());
        schedule.setEndAt(request.getStartAt().plusHours(1L));
        schedule.setExamination(examination);
        Schedule savedSchedule = _scheduleRepository.save(schedule);
        examination.setStatus(RequestType.CONFIRMING);
        examination.setEmergencyRoom(_emergencyRoomRepository.findOneById(request.getEmergencyRoomId()));
        examination.setSchedule(savedSchedule);
        Examination savedExamination = _examinationRepository.save(examination);

        return mapExaminationToExaminationResponse(savedExamination, savedSchedule);
    }

    @Override
    public Set<ExaminationResponse> getAllExaminations() {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.APPROVED)){
                examinations.add(allExaminations.get(i));
            }
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public ExaminationResponse getExamination(UUID id) {
        Examination examination = _examinationRepository.findOneById(id);
        return mapExaminationToExaminationResponse(examination, examination.getSchedule());
    }

    @Override
    public Set<ExaminationResponse> getAllExaminationByPatient(UUID id) {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.APPROVED) && allExaminations.get(i).getSchedule().getPatient().getId().equals(id)){
                examinations.add(allExaminations.get(i));
            }
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllExaminationByDoctor(UUID id) {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.APPROVED) && allExaminations.get(i).getSchedule().getDoctor().getId().equals(id)){
                examinations.add(allExaminations.get(i));
            }
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllPotentialExaminations() {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getSchedule().getPatient() == null){
                examinations.add(allExaminations.get(i));
            }
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllPendingExaminations() {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.PENDING)){
                examinations.add(allExaminations.get(i));
            }
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllConfirmingExaminationsByPatient(UUID id) {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.CONFIRMING) && allExaminations.get(i).getSchedule().getPatient().getId().equals(id)){
                examinations.add(allExaminations.get(i));
            }
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public ExaminationResponse approvePotentialExamination(ApprovePotentialExaminationRequest request) {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        examination.setStatus(RequestType.APPROVED);
        examination.getSchedule().setPatient(_patientRepository.findOneById(request.getPatientId()));
        examination.getSchedule().setReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        examination.getSchedule().setApproved(true);
        examination.getSchedule().getPatient().getDoctors().add(examination.getSchedule().getDoctor());
        examination.getSchedule().getDoctor().getPatients().add(examination.getSchedule().getPatient());
        examination.getSchedule().getDoctor().getSchedules().add(examination.getSchedule());
        Examination savedExamination = _examinationRepository.save(examination);

        return mapExaminationToExaminationResponse(savedExamination, savedExamination.getSchedule());
    }

    @Override
    public ExaminationResponse createExaminationRequestByDoctor(CreateExaminationRequestByDoctor request, UUID id) throws Exception {
        Patient patient = _patientRepository.findOneById(request.getPatientId());

        Doctor doctor = _doctorRepository.findOneById(id);
        Date now = new Date();
        LocalTime currentTime = request.getCurrentTime();
        boolean flag =  doctor.getSchedules().stream()
                .anyMatch(schedule -> schedule.getDate().getYear() == now.getYear()
                        && schedule.getDate().getMonth() == now.getMonth()
                        && schedule.getDate().getDay() == now.getDay()
                        && schedule.getStartAt().isBefore(currentTime)
                        && schedule.getEndAt().isAfter(currentTime)
                        && schedule.getPatient().getId().equals(patient.getId()));

        if(!flag) {
            throw new Exception("Trenutno ne vrsite pregled ovog pacijenta.");
        }

        Examination examination = new Examination();
        Schedule schedule = new Schedule();
        schedule.setDate(request.getDate());
        schedule.setStartAt(request.getStartAt());
        schedule.setEndAt(request.getStartAt().plusHours(1L));
        schedule.setDoctor(doctor);
        schedule.setPatient(patient);
        examination.setStatus(RequestType.PENDING);
        schedule.setExamination(examination);
        schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_EXAMINATION);
        schedule.setApproved(false);
        Schedule savedSchedule = _scheduleRepository.save(schedule);
        examination.setSchedule(savedSchedule);

        Examination savedExamination = _examinationRepository.save(examination);

        return mapExaminationToExaminationResponse(savedExamination, savedSchedule);
    }

    public ExaminationResponse mapExaminationToExaminationResponse(Examination examination, Schedule schedule){
        ExaminationResponse examinationResponse = new ExaminationResponse();
        examinationResponse.setId(examination.getId());
        examinationResponse.setDate(schedule.getDate());
        examinationResponse.setStartAt(schedule.getStartAt());
        examinationResponse.setEndAt(schedule.getEndAt());
        examinationResponse.setDoctorFirstName(schedule.getDoctor().getUser().getFirstName());
        examinationResponse.setDoctorLastName(schedule.getDoctor().getUser().getLastName());
        if(!(schedule.getPatient() == null)){
            examinationResponse.setPatientFirstName(schedule.getPatient().getUser().getFirstName());
            examinationResponse.setPatientLastName(schedule.getPatient().getUser().getLastName());
        }
        if(!(examination.getEmergencyRoom() == null)){
            examinationResponse.setEmergencyRoomName(examination.getEmergencyRoom().getName());
        }
        examinationResponse.setExaminationTypeName(schedule.getDoctor().getExaminationType().getName());
        examinationResponse.setClinicName(schedule.getDoctor().getClinic().getName());

        return examinationResponse;
    }
}
