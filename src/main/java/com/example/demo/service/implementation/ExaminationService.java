package com.example.demo.service.implementation;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.IEmailService;
import com.example.demo.service.IExaminationService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import com.example.demo.util.enums.RequestType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
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

    private final IEmailService _emailService;

    public ExaminationService(IExaminationRepository examinationRepository, IExaminationTypeRepository examinationTypeRepository, IEmergencyRoomRepository emergencyRoomRepository, IDoctorRepository doctorRepository, IPatientRepository patientRepository, IScheduleRepository scheduleRepository, IClinicRepository clinicRepository, IEmailService emailService) {
        _examinationRepository = examinationRepository;
        _examinationTypeRepository = examinationTypeRepository;
        _emergencyRoomRepository = emergencyRoomRepository;
        _doctorRepository = doctorRepository;
        _patientRepository = patientRepository;
        _scheduleRepository = scheduleRepository;
        _clinicRepository = clinicRepository;
        _emailService = emailService;
    }

    @Override
    public ExaminationResponse createExaminationRequestByPatient(CreateExaminationRequestByPatient request) throws Exception {

        Date now = new Date();
        if(request.getDate().before(now)){
            throw new Exception("Ovaj datum je prošao.");
        }

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

        for (Admin admin: doctor.getClinic().getAdmins()) {
            _emailService.announceAdminsAboutExaminationRequestMail(admin);
        }

        return mapExaminationToExaminationResponse(savedExamination, savedSchedule);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ExaminationResponse confirmExaminationRequestByAdmin(CreateExaminationRequestByAdmin request) {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        EmergencyRoom emergencyRoom = _emergencyRoomRepository.findOneById(request.getEmergencyRoomId());
        examination.setEmergencyRoom(emergencyRoom);
        examination.setStatus(RequestType.CONFIRMING);
        Examination savedExamination = _examinationRepository.save(examination);

        _emailService.approveExaminationToPatientMail(examination.getSchedule().getPatient());
        _emailService.approveExaminationToDoctorMail(examination.getSchedule().getDoctor());

        return mapExaminationToExaminationResponse(savedExamination, savedExamination.getSchedule());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public ExaminationResponse approveExamination(ApproveExaminationRequest request) throws Exception{
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());

        if(examination.getSchedule().getDoctor().getUser().isDeleted()){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Doktor je u medjuvremenu obrisan.");
        }

        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndNurse(true, null);

        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getId().equals(examination.getSchedule().getDoctor().getId())){
                if(schedules.get(i).getDate().getYear() == examination.getSchedule().getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == examination.getSchedule().getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == examination.getSchedule().getDate().getDay()) {
                    if(schedules.get(i).getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                        examination.setStatus(RequestType.DENIED);
                        _examinationRepository.save(examination);
                        throw new Exception("Doktor je na godišnjem odmoru.");
                    }
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt())) ) {
                        flag = true;
                        break;
                    }

                    if(examination.getSchedule().getStartAt().toString().equals(schedules.get(i).getStartAt().toString())){
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Doktor je u medjuvremenu zauzet.");
        }

        schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getExamination().getEmergencyRoom().getId().equals(examination.getEmergencyRoom().getId())){
                if(schedules.get(i).getDate().getYear() == examination.getSchedule().getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == examination.getSchedule().getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == examination.getSchedule().getDate().getDay()) {
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt())) ) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Sala je u medjuvremenu zauzeta.");
        }

        examination.setStatus(RequestType.APPROVED);
        examination.getSchedule().setReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        examination.getSchedule().setApproved(true);
        examination.getSchedule().getPatient().getDoctors().add(examination.getSchedule().getDoctor());
        examination.getSchedule().getDoctor().getPatients().add(examination.getSchedule().getPatient());
        examination.getSchedule().getDoctor().getSchedules().add(examination.getSchedule());
        Examination savedExamination = _examinationRepository.save(examination);

        return mapExaminationToExaminationResponse(savedExamination, savedExamination.getSchedule());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void denyExaminationRequest(DenyExaminationRequest request) {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        examination.setStatus(RequestType.DENIED);
        _examinationRepository.save(examination);

        if(!request.getReason().equals("")){
            _emailService.denyExaminationToPatientMail(examination.getSchedule().getPatient(), request.getReason());
            _emailService.denyExaminationToDoctorMail(examination.getSchedule().getDoctor(), request.getReason());
        }
    }

    @Override
    public ExaminationResponse createPotentialExamination(CreatePotentialExaminationRequest request) throws Exception {
        Date now = new Date();
        if(request.getDate().before(now)){
            throw new Exception("Datum je prošao.");
        }

        Examination examination = new Examination();
        Schedule schedule = new Schedule();
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndNurse(true, null);
        boolean flag = false;
        if(request.getStartAt().isBefore(_doctorRepository.findOneById(request.getDoctorId()).getStartAt())
            || request.getStartAt().plusHours(1L).isAfter(_doctorRepository.findOneById(request.getDoctorId()).getEndAt())){
            throw new Exception("Doktor ne radi u tom terminu.");
        }
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getId().equals(request.getDoctorId())){
                if(schedules.get(i).getDate().getYear() == request.getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == request.getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == request.getDate().getDay()) {
                    if(schedules.get(i).getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                        examination.setStatus(RequestType.DENIED);
                        _examinationRepository.save(examination);
                        throw new Exception("Doktor je na godišnjem odmoru.");
                    }
                    if ((request.getStartAt().plusHours(1L).isBefore(schedules.get(i).getEndAt()) && request.getStartAt().plusHours(1L).isAfter(schedules.get(i).getStartAt()))
                            || (request.getStartAt().isBefore(schedules.get(i).getEndAt()) && request.getStartAt().isAfter(schedules.get(i).getStartAt())) ) {
                        flag = true;
                        break;
                    }
                    String startAtString = examination.getSchedule().getStartAt().toString();
                    String[] startAtTokens = startAtString.split(":");
                    if(examination.getSchedule().getStartAt().toString().equals(schedules.get(i).getStartAt().toString())){
                        flag = true;
                        break;
                    }
                    if(Integer.parseInt(startAtTokens[0]) == 23){
                        throw new Exception("Ne možete rezervisati pregled posle 23h.");
                    }
                }
            }
        }
        if(flag){
            throw new Exception("Ne mozete rezervisati pregled u ovom terminu, doktor je tada zauzet.");
        }
        schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getExamination().getEmergencyRoom().getId().equals(request.getEmergencyRoomId())){
                if(schedules.get(i).getDate().getYear() == request.getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == request.getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == request.getDate().getDay()) {
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt())) ) {
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
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)){
                    examinations.add(allExaminations.get(i));
                }
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
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    examinations.add(allExaminations.get(i));
                }
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
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    examinations.add(allExaminations.get(i));
                }
            }
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllPotentialExaminations() throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        Date now = new Date();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getSchedule().getPatient() == null && now.before(allExaminations.get(i).getSchedule().getDate())){
                examinations.add(allExaminations.get(i));
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Ne postoji nijedan potencijalni pregled.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllPendingExaminations() throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.PENDING)){
                if(allExaminations.get(i).getSchedule().getStartAt() != null) {
                    examinations.add(allExaminations.get(i));
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Nemate nijedan zahtev za pregled.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllConfirmingExaminationsByPatient(UUID id) throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getSchedule().getPatient() != null) {
                if (allExaminations.get(i).getStatus().equals(RequestType.CONFIRMING) && allExaminations.get(i).getSchedule().getPatient().getId().equals(id)) {
                    examinations.add(allExaminations.get(i));
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Nemate nijedan potencijalni pregled.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ExaminationResponse approvePotentialExamination(ApprovePotentialExaminationRequest request) throws Exception{
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());

        if(examination.getSchedule().getDoctor().getUser().isDeleted()){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Doktor je u medjuvremenu obrisan.");
        }

        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndNurse(true, null);
        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getId().equals(examination.getSchedule().getDoctor().getId())){
                if(schedules.get(i).getDate().getYear() == examination.getSchedule().getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == examination.getSchedule().getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == examination.getSchedule().getDate().getDay()) {
                    if(schedules.get(i).getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                        examination.setStatus(RequestType.DENIED);
                        _examinationRepository.save(examination);
                        throw new Exception("Doktor je na godišnjem odmoru.");
                    }
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt())) ) {
                        flag = true;
                        break;
                    }

                    String startAtString = examination.getSchedule().getStartAt().toString();
                    String[] startAtTokens = startAtString.split(":");
                    if(examination.getSchedule().getStartAt().toString().equals(schedules.get(i).getStartAt().toString())){
                        flag = true;
                        break;
                    }

                    if(Integer.parseInt(startAtTokens[0]) == 23){
                        throw new Exception("Ne možete zakazati pregled posle 23h.");
                    }
                }
            }
        }
        if(flag){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Doktor je u medjuvremenu zauzet.");
        }
        schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getExamination().getEmergencyRoom().getId().equals(examination.getEmergencyRoom().getId())){
                if(schedules.get(i).getDate().getYear() == examination.getSchedule().getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == examination.getSchedule().getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == examination.getSchedule().getDate().getDay()) {
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt())) ) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Sala je u medjuvremenu zauzeta.");
        }

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

        Date now = new Date();
        if(request.getDate().before(now)){
            throw new Exception("Ovaj datum je prošao.");
        }

        Doctor doctor = _doctorRepository.findOneById(id);
        LocalTime currentTime = request.getCurrentTime();
        boolean flag =  doctor.getSchedules().stream()
                .anyMatch(schedule -> schedule.getDate().getYear() == now.getYear()
                        && schedule.getDate().getMonth() == now.getMonth()
                        && schedule.getDate().getDay() == now.getDay()
                        && schedule.getStartAt().isBefore(currentTime)
                        && schedule.getEndAt().isAfter(currentTime)
                        && schedule.getPatient().getId().equals(patient.getId()));

        if(!flag) {
            throw new Exception("Trenutno ne vršite pregled ovog pacijenta.");
        }

        String startAtString = request.getStartAt().toString();
        String[] startAtTokens = startAtString.split(":");
        if(Integer.parseInt(startAtTokens[0]) == 23){
            throw new Exception("Ne možete zakazati pregled posle 23h.");
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

        for (Admin admin: doctor.getClinic().getAdmins()) {
            _emailService.announceAdminsAboutExaminationRequestMail(admin);
        }

        return mapExaminationToExaminationResponse(savedExamination, savedSchedule);
    }

    @Override
    public Set<ExaminationResponse> getAllPotentialExaminationsByClinic(UUID clinicId) throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        for(int i = 0;i < allExaminations.size();i++) {
            if (allExaminations.get(i).getSchedule().getPatient() == null && clinic == allExaminations.get(i).getSchedule().getDoctor().getClinic() && allExaminations.get(i).getStatus().equals(RequestType.CONFIRMING) && allExaminations.get(i).getSchedule().getReasonOfUnavailability().equals(ReasonOfUnavailability.POTENTIAL_EXAMINATION)) {
                examinations.add(allExaminations.get(i));
            }
        }

        if(examinations.isEmpty()){
            throw new Exception("Ne postoji nijedan potencijalni pregled.");
        }
        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public void deletePotentialExamination(UUID id) {
        Examination examination = _examinationRepository.findOneById(id);
        examination.setStatus(RequestType.DENIED);
        _examinationRepository.save(examination);
    }

    @Override
    public Set<ExaminationResponse> getAllPendingExaminationsByClinic(UUID clinicId) throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.PENDING) && allExaminations.get(i).getSchedule().getDoctor().getClinic() == clinic){
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    examinations.add(allExaminations.get(i));
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Nemate nijedan zahtev za pregled.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public void cancelExamination(UUID examinationId) throws Exception {
        Examination examination = _examinationRepository.findOneById(examinationId);
        Date now = new Date();
        if(examination.getSchedule().getDate().before(now)){
            throw new Exception("Pregled možete otkazati najkasnije 24h pre samog termina.");
        }
        examination.setStatus(RequestType.DENIED);
        examination.getSchedule().setApproved(false);
        examination.getSchedule().setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_EXAMINATION);
        _scheduleRepository.save(examination.getSchedule());
        _examinationRepository.save(examination);
    }

    @Override
    public Set<ExaminationResponse> getPatientsExaminationHistory(UUID patientId) throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndPatientId(true, patientId);
        Date now = new Date();
        Set<Examination> patientsHistory = new HashSet<>();
        for (Schedule s: schedules) {
            if(s.getDate().before(now)){
                String startAtString = s.getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = s.getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    patientsHistory.add(s.getExamination());
                }
            }
        }
        if(patientsHistory.isEmpty()){
            throw new Exception("Niste imali nijedan pregled do danas.");
        }
        return patientsHistory.stream()
                .map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getExaminationsWhichPatientCanCancel(UUID patientId) throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndPatientId(true, patientId);
        Date now = new Date();
        Set<Examination> examinations = new HashSet<>();
        for (Schedule s: schedules) {
            if(s.getDate().after(now)){
                String startAtString = s.getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = s.getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    examinations.add(s.getExamination());
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Trenutno nemate nijedan zakazan pregled.");
        }
        return examinations.stream()
                .map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getExaminationsWhichDoctorCanCancel(UUID doctorId) throws Exception {
        Set<Examination> allExaminations = _examinationRepository.findAllByStatus(RequestType.APPROVED);
        Date now = new Date();
        Set<Examination> examinations = new HashSet<>();
        for (Examination e: allExaminations) {
            if(e.getSchedule().getDate().after(now) && e.getSchedule().getDoctor().getId().equals(doctorId)){
                String startAtString = e.getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = e.getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    examinations.add(e);
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Trenutno nemate nijedan zakazan pregled.");
        }

        return examinations.stream()
                .map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getPatientsExaminationHistory(SearchPatientsExaminationHistoryRequest request, UUID patientId) throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndPatientId(true, patientId);
        Date now = new Date();
        Set<Examination> patientsHistory = new HashSet<>();
        for (Schedule s: schedules) {
            if(s.getDate().before(now)){
                String startAtString = s.getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = s.getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    patientsHistory.add(s.getExamination());
                }
            }
        }

        Set<Examination> serchedByName = new HashSet<>();

        for(Examination examination: patientsHistory){
            if(examination.getSchedule().getDoctor().getExaminationType().getName().toLowerCase().contains(request.getName().toLowerCase())){
                serchedByName.add(examination);
            }
        }

        return serchedByName.stream()
                .map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public ExaminationResponse createOperationRequest(CreateOperationRequest request) throws Exception {
        Patient patient = _patientRepository.findOneById(request.getPatientId());

        Date now = new Date();
        if(request.getDate().before(now)){
            throw new Exception("Ovaj datum je prošao.");
        }

        Doctor doctor = _doctorRepository.findOneById(request.getDoctorId());
        LocalTime currentTime = request.getCurrentTime();
        boolean flag =  doctor.getSchedules().stream()
                .anyMatch(schedule -> schedule.getDate().getYear() == now.getYear()
                        && schedule.getDate().getMonth() == now.getMonth()
                        && schedule.getDate().getDay() == now.getDay()
                        && schedule.getStartAt().isBefore(currentTime)
                        && schedule.getEndAt().isAfter(currentTime)
                        && schedule.getPatient().getId().equals(patient.getId()));

        if(!flag) {
            throw new Exception("Trenutno ne vršite pregled ovog pacijenta.");
        }

        String startAtString = request.getStartAt().toString();
        String[] startAtTokens = startAtString.split(":");
        if(Integer.parseInt(startAtTokens[0]) >= 22){
            throw new Exception("Ne možete zakazati operaciju posle 22h.");
        }

        Examination examination = new Examination();
        Schedule schedule = new Schedule();
        schedule.setDate(request.getDate());
        schedule.setDoctor(doctor);
        schedule.setPatient(patient);
        examination.setStatus(RequestType.PENDING);
        schedule.setExamination(examination);
        schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_EXAMINATION);
        schedule.setApproved(false);
        schedule.setStartAt(request.getStartAt());
        schedule.setEndAt(request.getStartAt().plusHours(2L));
        Schedule savedSchedule = _scheduleRepository.save(schedule);
        examination.setSchedule(savedSchedule);

        Examination savedExamination = _examinationRepository.save(examination);

        for (Admin admin: doctor.getClinic().getAdmins()) {
            _emailService.announceAdminsAboutOperationRequestMail(admin);
        }

        return mapExaminationToExaminationResponse(savedExamination, savedSchedule);
    }

//    public ExaminationResponse setTimeForAnOperation(SetTimeForAnOperation request) throws Exception {
//        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
//        if(examination.getSchedule().getDoctor().getUser().isDeleted()){
//            examination.setStatus(RequestType.DENIED);
//            _examinationRepository.save(examination);
//            throw new Exception("Doktor je u medjuvremenu obrisan.");
//        }
//
//        String startAtString = request.getStartAt().toString();
//        String[] startAtTokens = startAtString.split(":");
//        if(Integer.parseInt(startAtTokens[0]) >= 22){
//            throw new Exception("Ne možete zakazati operaciju posle 22h.");
//        }
//
//        if(!(request.getStartAt().isAfter(examination.getSchedule().getDoctor().getStartAt()) && request.getStartAt().plusHours(2L).isBefore(examination.getSchedule().getDoctor().getEndAt()))){
//            throw new Exception("Doktor ne radi u tom terminu.");
//        }
//
//        examination.getSchedule().setStartAt(request.getStartAt());
//        examination.getSchedule().setEndAt(request.getStartAt().plusHours(2L));
//        _examinationRepository.save(examination);
//
//        return mapExaminationToExaminationResponse(examination, examination.getSchedule());
//    }

    @Override
    public ExaminationResponse approveOperation(ApproveOperationRequest request) throws Exception {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        examination.setEmergencyRoom(_emergencyRoomRepository.findOneById(request.getEmergencyRoomId()));

        if(examination.getSchedule().getDoctor().getUser().isDeleted()){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Doktor je u medjuvremenu obrisan.");
        }
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndNurse(true, null);

        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getId().equals(examination.getSchedule().getDoctor().getId())){
                if(schedules.get(i).getDate().getYear() == examination.getSchedule().getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == examination.getSchedule().getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == examination.getSchedule().getDate().getDay()) {
                    if(schedules.get(i).getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                        examination.setStatus(RequestType.DENIED);
                        _examinationRepository.save(examination);
                        throw new Exception("Doktor je na godišnjem odmoru.");
                    }
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt()))) {
                        flag = true;
                        break;
                    }

                    if(examination.getSchedule().getStartAt().toString().equals(schedules.get(i).getStartAt().toString())){
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Doktor je tada zauzet.");
        }

        schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getExamination().getEmergencyRoom().getId().equals(examination.getEmergencyRoom().getId())){
                if(schedules.get(i).getDate().getYear() == examination.getSchedule().getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == examination.getSchedule().getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == examination.getSchedule().getDate().getDay()) {
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt())) ) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            examination.setStatus(RequestType.DENIED);
            _examinationRepository.save(examination);
            throw new Exception("Sala je zauzeta.");
        }

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
    public void denyOperation(DenyOperationRequest request) throws Exception {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        examination.setStatus(RequestType.DENIED);
        _examinationRepository.save(examination);

        if(!request.getReason().equals("")){
            _emailService.denyOperationToPatientMail(examination.getSchedule().getPatient(), request.getReason());
            _emailService.denyOperationToDoctorMail(examination.getSchedule().getDoctor(), request.getReason());
        }
    }

    @Override
    public Set<ExaminationResponse> getAllPendingOperations() throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.PENDING)){
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) != -1)) {
                    examinations.add(allExaminations.get(i));
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Nemate nijedan zahtev za operaciju.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getAllPendingOperationsByClinic(UUID clinicId) throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        Set<Examination> examinations = new HashSet<>();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.PENDING) && allExaminations.get(i).getSchedule().getDoctor().getClinic() == clinic){
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) != -1)) {
                    examinations.add(allExaminations.get(i));
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Nemate nijedan zahtev za operaciju.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getPatientsOperationHistory(UUID patientId) throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndPatientId(true, patientId);
        Date now = new Date();
        Set<Examination> patientsHistory = new HashSet<>();
        for (Schedule s: schedules) {
            if(s.getDate().before(now)){
                String startAtString = s.getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = s.getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) != -1)) {
                    patientsHistory.add(s.getExamination());
                }
            }
        }
        if(patientsHistory.isEmpty()){
            throw new Exception("Niste imali nijednu operaciju do danas.");
        }
        return patientsHistory.stream()
                .map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getOperationsWhichPatientCanCancel(UUID patientId) throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndPatientId(true, patientId);
        Date now = new Date();
        Set<Examination> examinations = new HashSet<>();
        for (Schedule s: schedules) {
            if(s.getDate().after(now)){
                String startAtString = s.getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = s.getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) != -1)) {
                    examinations.add(s.getExamination());
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Trenutno nemate nijednu zakazanu operaciju.");
        }
        return examinations.stream()
                .map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getOperationsWhichDoctorCanCancel(UUID doctorId) throws Exception {
        Set<Examination> allExaminations = _examinationRepository.findAllByStatus(RequestType.APPROVED);
        Date now = new Date();
        Set<Examination> examinations = new HashSet<>();
        for (Examination e: allExaminations) {
            if(e.getSchedule().getDate().after(now) && e.getSchedule().getDoctor().getId().equals(doctorId)){
                String startAtString = e.getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = e.getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) != -1)) {
                    examinations.add(e);
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Trenutno nemate nijednu zakazanu operaciju.");
        }

        return examinations.stream()
                .map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public void cancelOperation(UUID id) throws Exception {
        Examination examination = _examinationRepository.findOneById(id);
        Date now = new Date();
        if(examination.getSchedule().getDate().before(now)){
            throw new Exception("Operaciju možete otkazati najkasnije 24h pre samog termina.");
        }
        examination.setStatus(RequestType.DENIED);
        examination.getSchedule().setApproved(false);
        examination.getSchedule().setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_EXAMINATION);
        _scheduleRepository.save(examination.getSchedule());
        _examinationRepository.save(examination);
    }

    @Override
    public Set<ExaminationResponse> getFutureOperationsByAdmin(UUID clinicId) throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        Set<Examination> examinations = new HashSet<>();
        Date now = new Date();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.APPROVED) && allExaminations.get(i).getSchedule().getDoctor().getClinic() == clinic  && now.before(allExaminations.get(i).getSchedule().getDate())){
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) != -1)) {
                    boolean flag = true;
                    for(Examination e: examinations){
                        if(e.getSchedule().getStartAt().toString().equals(allExaminations.get(i).getSchedule().getStartAt().toString()) && e.getSchedule().getEndAt().toString().equals(allExaminations.get(i).getSchedule().getEndAt().toString()) && e.getSchedule().getPatient() == allExaminations.get(i).getSchedule().getPatient() && e.getSchedule().getDate().getYear() == allExaminations.get(i).getSchedule().getDate().getYear() && e.getSchedule().getDate().getMonth() == allExaminations.get(i).getSchedule().getDate().getMonth() && e.getSchedule().getDate().getDay() == allExaminations.get(i).getSchedule().getDate().getDay()){
                            flag = false;
                        }
                    }
                    if(flag){
                        examinations.add(allExaminations.get(i));
                    }
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Klinika nema nijednu zakazanu operaciju.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationResponse> getFutureExaminationsByAdmin(UUID clinicId) throws Exception {
        List<Examination> allExaminations = _examinationRepository.findAll();
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        Set<Examination> examinations = new HashSet<>();
        Date now = new Date();
        for(int i = 0;i < allExaminations.size();i++){
            if(allExaminations.get(i).getStatus().equals(RequestType.APPROVED) && allExaminations.get(i).getSchedule().getDoctor().getClinic() == clinic && now.before(allExaminations.get(i).getSchedule().getDate())){
                String startAtString = allExaminations.get(i).getSchedule().getStartAt().toString();
                String[] startAtTokens = startAtString.split(":");
                String startEndString = allExaminations.get(i).getSchedule().getEndAt().toString();
                String[] startEndTokens = startEndString.split(":");
                if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) == -1)) {
                    examinations.add(allExaminations.get(i));
                }
            }
        }
        if(examinations.isEmpty()){
            throw new Exception("Klinika nema nijedan zakazan pregled.");
        }

        return examinations.stream().map(examination -> mapExaminationToExaminationResponse(examination, examination.getSchedule()))
                .collect(Collectors.toSet());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ExaminationResponse assignDoctor(AssignDoctorRequest request) throws Exception {
        Doctor doctor = _doctorRepository.findOneById(request.getDoctorId());
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());

        if(!(examination.getSchedule().getStartAt().isAfter(doctor.getStartAt()) && examination.getSchedule().getEndAt().isBefore(doctor.getEndAt()))){
            throw new Exception("Doktor tada ne radi.");
        }

        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndNurse(true, null);
        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getId().equals(doctor.getId())){
                if(schedules.get(i).getDate().getYear() == examination.getSchedule().getDate().getYear()
                        && schedules.get(i).getDate().getMonth() == examination.getSchedule().getDate().getMonth()
                        && schedules.get(i).getDate().getDay() == examination.getSchedule().getDate().getDay()) {
                    if(schedules.get(i).getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                        throw new Exception("Doktor je na godišnjem odmoru.");
                    }
                    if ((examination.getSchedule().getEndAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getEndAt().isAfter(schedules.get(i).getStartAt()))
                            || (examination.getSchedule().getStartAt().isBefore(schedules.get(i).getEndAt()) && examination.getSchedule().getStartAt().isAfter(schedules.get(i).getStartAt()))) {
                        flag = true;
                        break;
                    }

                    if(examination.getSchedule().getStartAt().toString().equals(schedules.get(i).getStartAt().toString())){
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            throw new Exception("Doktor je tada zauzet.");
        }

        Examination newExamination = new Examination();
        newExamination.setStatus(RequestType.APPROVED);
        newExamination.setEmergencyRoom(examination.getEmergencyRoom());
        Schedule schedule = new Schedule();
        schedule.setStartAt(examination.getSchedule().getStartAt());
        schedule.setEndAt(examination.getSchedule().getEndAt());
        schedule.setReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        schedule.setApproved(true);
        schedule.setDate(examination.getSchedule().getDate());
        schedule.setDoctor(doctor);
        schedule.setPatient(examination.getSchedule().getPatient());
        schedule.setExamination(newExamination);
        Schedule savedSchedule = _scheduleRepository.save(schedule);
        newExamination.setSchedule(savedSchedule);
        Examination savedExamination = _examinationRepository.save(newExamination);

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
