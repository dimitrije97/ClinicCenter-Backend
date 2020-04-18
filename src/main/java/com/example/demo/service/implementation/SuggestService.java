package com.example.demo.service.implementation;

import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.entity.EmergencyRoom;
import com.example.demo.entity.Examination;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IExaminationRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.ISuggestService;
import com.example.demo.util.enums.RequestType;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SuggestService implements ISuggestService {

    private final IExaminationRepository _examinationRepository;

    private final IScheduleRepository _scheduleRepository;

    public SuggestService(IExaminationRepository examinationRepository, IScheduleRepository scheduleRepository) {
        _examinationRepository = examinationRepository;
        _scheduleRepository = scheduleRepository;
    }

    @Override
    public ExaminationResponse suggest(UUID id) throws Exception {
        Examination examination = _examinationRepository.findOneById(id);
        List<EmergencyRoom> emergencyRooms = examination.getSchedule().getDoctor().getClinic().getEmergencyRooms();
        long hour = 1;
        LocalTime currentTime = examination.getSchedule().getStartAt();
        while(true){
            currentTime = currentTime.plusHours(hour);
            List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndDoctorAndDate(true, examination.getSchedule().getDoctor(), examination.getSchedule().getDate());
            boolean flag = false;
            for (Schedule s: schedules) {
                if(currentTime.isAfter(s.getStartAt().minusHours(1L)) && currentTime.isBefore(s.getEndAt())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                continue;
            }
            Set<Examination> examinations = _examinationRepository.findAllByStatus(RequestType.APPROVED);
            for (EmergencyRoom er: emergencyRooms) {
                boolean isAvailable = true;
                for (Examination e: examinations) {
                    if(er == e.getEmergencyRoom() && currentTime.isAfter(e.getSchedule().getStartAt().minusHours(1L)) && currentTime.isBefore(e.getSchedule().getEndAt())){
                        isAvailable = false;
                    }
                    if(isAvailable){
                        examination.getSchedule().setStartAt(currentTime);
                        examination.setEmergencyRoom(er);
                        examination.setStatus(RequestType.CONFIRMING);
                        Examination savedExamination = _examinationRepository.save(examination);
                        return mapExaminationToExaminationResponse(savedExamination, savedExamination.getSchedule());
                    }
                }
            }
            hour++;
            if(hour == 23){
                Date currentDate = examination.getSchedule().getDate();
                Date tomorrow = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24));
                examination.getSchedule().setDate(tomorrow);
                _examinationRepository.save(examination);
                suggest(id);
            }
        }
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
