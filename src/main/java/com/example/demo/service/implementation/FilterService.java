package com.example.demo.service.implementation;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.EmergencyRoomResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.IFilterService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilterService implements IFilterService {

    private final IClinicRepository _clinicRepository;

    private final IExaminationTypeRepository _examinationTypeRepository;

    private final IDoctorRepository _doctorRepository;

    private final IEmergencyRoomRepository _emergencyRoomRepository;

    private final IScheduleRepository _scheduleRepository;

    private final IExaminationRepository _examinationRepository;

    public FilterService(IClinicRepository clinicRepository, IExaminationTypeRepository examinationTypeRepository, IDoctorRepository doctorRepository, IEmergencyRoomRepository emergencyRoomRepository, IScheduleRepository scheduleRepository, IExaminationRepository examinationRepository) {
        _clinicRepository = clinicRepository;
        _examinationTypeRepository = examinationTypeRepository;
        _doctorRepository = doctorRepository;
        _emergencyRoomRepository = emergencyRoomRepository;
        _scheduleRepository = scheduleRepository;
        _examinationRepository = examinationRepository;
    }

    @Override
    public Set<ClinicResponse> getClinicsByDateAndExamnationType(AvailableClinicsRequest request) throws Exception {
        ExaminationType examinationType = _examinationTypeRepository.findOneById(request.getExaminationTypeId());
        Set<Clinic> allClinics = _clinicRepository.findAllByDeleted(false);
        Set<Clinic> clinics = new HashSet<>();

        Date now = new Date();
        if(now.after(request.getDate())){
            throw new Exception("Ovaj datum je prošao.");
        }

        for (Clinic c: allClinics) {
            for (Doctor d: c.getDoctors()) {
                if(d.getExaminationType() == examinationType){
                    boolean flag = false;
                    for (Schedule s: d.getSchedules()) {
                        if(request.getDate().getYear() == s.getDate().getYear()
                            && request.getDate().getMonth() == s.getDate().getMonth()
                            && request.getDate().getDay() == s.getDate().getDay()){
                            if(s.getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                                flag = true;
                            }
                        }
                        if(flag){
                            break;
                        }
                    }
                    if(!flag){
                        clinics.add(c);
                        break;
                    }
                }
            }
        }

        if(clinics.isEmpty()){
            throw new Exception("Ne mozete zakazati takav pregled ni u jednoj klinici kliničkog centra.");
        }

        return clinics.stream().map(clinic -> mapClinicToClinicResponse(clinic))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinic(AvailableDoctorsRequest request) throws Exception {
        Clinic clinic = _clinicRepository.findOneById(request.getClinicId());
        ExaminationType examinationType = _examinationTypeRepository.findOneById(request.getExaminationTypeId());
        List<Doctor> allDoctors = clinic.getDoctors();
        Set<Doctor> doctors = new HashSet<>();

        String[] tokens = request.getStartAt().split(":");
        LocalTime startAt =  LocalTime.of(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
        if(tokens[0].equals("23")){
            throw new Exception("Ne možete zakazati pregled između 23h i ponoći.");
        }

        for (Doctor d: allDoctors) {
            if(d.getExaminationType() == examinationType && startAt.isAfter(d.getStartAt()) && startAt.plusHours(1L).isBefore(d.getEndAt())){
                boolean flag = false;
                for (Schedule s: d.getSchedules()) {
                    if(request.getDate().getYear() == s.getDate().getYear()
                            && request.getDate().getMonth() == s.getDate().getMonth()
                            && request.getDate().getDay() == s.getDate().getDay()){
                        if(s.getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                            flag = true;
                        }
                        if(s.getReasonOfUnavailability().equals(ReasonOfUnavailability.EXAMINATION)
                                && startAt.isAfter(s.getStartAt().minusHours(1L))
                                && startAt.isBefore(s.getEndAt())){
                            flag = true;
                        }
                    }
                    if(flag){
                        break;
                    }
                }
                if(!flag){
                    doctors.add(d);
                }
            }
        }

        if(doctors.isEmpty()){
            throw new Exception("Ne možete zakazati takav pregled.");
        }

        return doctors.stream().map(doctor -> mapDoctorToDoctorResponse(doctor))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EmergencyRoomResponse> getAvailableEmergencyRooms(AvailableEmergencyRoomsRequest request) throws Exception {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        Clinic clinic = examination.getSchedule().getDoctor().getClinic();
        List<EmergencyRoom> allEmergencyRooms = clinic.getEmergencyRooms();
        Set<EmergencyRoom> emergencyRooms = new HashSet<>();
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);

        for (EmergencyRoom er: allEmergencyRooms) {
            boolean flag = false;
            for (Schedule s: schedules) {
                if(examination.getSchedule().getDate().getYear() == s.getDate().getYear()
                        && examination.getSchedule().getDate().getMonth() == s.getDate().getMonth()
                        && examination.getSchedule().getDate().getDay() == s.getDate().getDay()
                        && examination.getSchedule().getStartAt().isAfter(s.getStartAt().minusHours(1L))
                        && examination.getSchedule().getStartAt().isBefore(s.getEndAt())
                        && s.getExamination().getEmergencyRoom() == er){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                emergencyRooms.add(er);
            }
        }

        if(emergencyRooms.isEmpty()){
            throw new Exception("Nijedna sala nije slobodna u tom terminu.");
        }

        return emergencyRooms.stream().map(emergencyRoom -> mapEmergencyRoomToEmergencyRoomResponse(emergencyRoom))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EmergencyRoomResponse> getAvailableEmergencyRoomsOp(AvailableEmergencyRoomsRequest request) throws Exception {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        Clinic clinic = examination.getSchedule().getDoctor().getClinic();
        List<EmergencyRoom> allEmergencyRooms = clinic.getEmergencyRooms();
        Set<EmergencyRoom> emergencyRooms = new HashSet<>();
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);

        for (EmergencyRoom er: allEmergencyRooms) {
            boolean flag = false;
            for (Schedule s: schedules) {
                if(examination.getSchedule().getDate().getYear() == s.getDate().getYear()
                        && examination.getSchedule().getDate().getMonth() == s.getDate().getMonth()
                        && examination.getSchedule().getDate().getDay() == s.getDate().getDay()
                        && examination.getSchedule().getStartAt().isAfter(s.getStartAt().minusHours(2L))
                        && examination.getSchedule().getStartAt().isBefore(s.getEndAt())
                        && s.getExamination().getEmergencyRoom() == er){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                emergencyRooms.add(er);
            }
        }

        if(emergencyRooms.isEmpty()){
            throw new Exception("Nijedna sala nije slobodna u tom terminu.");
        }

        return emergencyRooms.stream().map(emergencyRoom -> mapEmergencyRoomToEmergencyRoomResponse(emergencyRoom))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DoctorResponse> getDoctorsByDateAndStartAtAndExaminationTypeAndClinicByFirstNameAndLastName(SearchAvailableDoctorsRequest request) throws Exception {
        Clinic clinic = _clinicRepository.findOneById(request.getClinicId());
        ExaminationType examinationType = _examinationTypeRepository.findOneById(request.getExaminationTypeId());
        List<Doctor> allDoctors = clinic.getDoctors();
        Set<Doctor> doctors = new HashSet<>();

        String[] tokens = request.getStartAt().split(":");
        LocalTime startAt =  LocalTime.of(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
//        if(tokens[0].equals("23")){
//            throw new Exception("Ne možete zakazati pregled između 23h i ponoći.");
//        }

        for (Doctor d: allDoctors) {
            if(d.getExaminationType() == examinationType && startAt.isAfter(d.getStartAt()) && startAt.plusHours(1L).isBefore(d.getEndAt())){
                boolean flag = false;
                for (Schedule s: d.getSchedules()) {
                    if(request.getDate().getYear() == s.getDate().getYear()
                            && request.getDate().getMonth() == s.getDate().getMonth()
                            && request.getDate().getDay() == s.getDate().getDay()){
                        if(s.getReasonOfUnavailability().equals(ReasonOfUnavailability.VACATION)){
                            flag = true;
                        }
                        if(s.getReasonOfUnavailability().equals(ReasonOfUnavailability.EXAMINATION)
                                && startAt.isAfter(s.getStartAt().minusHours(1L))
                                && startAt.isBefore(s.getEndAt())){
                            flag = true;
                        }
                    }
                    if(flag){
                        break;
                    }
                }
                if(!flag){
                    doctors.add(d);
                }
            }
        }

        Set<Doctor> searchedByFirstName = new HashSet<>();
        Set<Doctor> searchedByFirstNameAndLastName = new HashSet<>();

        for(Doctor doctor: doctors){
            if(doctor.getUser().getFirstName().toLowerCase().contains(request.getFirstName().toLowerCase())){
                searchedByFirstName.add(doctor);
            }
        }

        for(Doctor doctor: searchedByFirstName){
            if(doctor.getUser().getLastName().toLowerCase().contains(request.getLastName().toLowerCase())){
                searchedByFirstNameAndLastName.add(doctor);
            }
        }

        return searchedByFirstNameAndLastName.stream()
                .map(doctor -> mapDoctorToDoctorResponse(doctor))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EmergencyRoomResponse> getAvailableEmergencyRooms(SearchAvailableEmergencyRoomsRequest request) throws Exception {
        Examination examination = _examinationRepository.findOneById(request.getExaminationId());
        Clinic clinic = examination.getSchedule().getDoctor().getClinic();
        List<EmergencyRoom> allEmergencyRooms = clinic.getEmergencyRooms();
        Set<EmergencyRoom> emergencyRooms = new HashSet<>();
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);

        for (EmergencyRoom er: allEmergencyRooms) {
            boolean flag = false;
            for (Schedule s: schedules) {
                if(examination.getSchedule().getDate().getYear() == s.getDate().getYear()
                        && examination.getSchedule().getDate().getMonth() == s.getDate().getMonth()
                        && examination.getSchedule().getDate().getDay() == s.getDate().getDay()
                        && examination.getSchedule().getStartAt().isAfter(s.getStartAt().minusHours(1L))
                        && examination.getSchedule().getStartAt().isBefore(s.getEndAt())
                        && s.getExamination().getEmergencyRoom() == er){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                emergencyRooms.add(er);
            }
        }

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

    public ClinicResponse mapClinicToClinicResponse(Clinic clinic){
        ClinicResponse clinicResponse = new ClinicResponse();

        clinicResponse.setAddress(clinic.getAddress());
        clinicResponse.setDescription(clinic.getDescription());
        clinicResponse.setName(clinic.getName());
        clinicResponse.setId(clinic.getId());

        return clinicResponse;
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
        doctorResponse.setStartAt(doctor.getStartAt());
        doctorResponse.setEndAt(doctor.getEndAt());

        return doctorResponse;
    }

    public EmergencyRoomResponse mapEmergencyRoomToEmergencyRoomResponse(EmergencyRoom emergencyRoom){
        EmergencyRoomResponse emergencyRoomResponse = new EmergencyRoomResponse();
        emergencyRoomResponse.setId(emergencyRoom.getId());
        emergencyRoomResponse.setName(emergencyRoom.getName());
        emergencyRoomResponse.setNumber(emergencyRoom.getNumber());
        return emergencyRoomResponse;
    }
}
