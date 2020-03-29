package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateVacationRequest;
import com.example.demo.dto.response.VacationResponse;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.repository.INurseRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IVacationService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VacationService implements IVacationService {

    private final IScheduleRepository _scheduleRepository;

    private final IDoctorRepository _doctorRepository;

    private final INurseRepository _nurseRepository;

    public VacationService(IScheduleRepository scheduleRepository, IDoctorRepository doctorRepository, INurseRepository nurseRepository) {
        _scheduleRepository = scheduleRepository;
        _doctorRepository = doctorRepository;
        _nurseRepository = nurseRepository;
    }

    @Override
    public Set<VacationResponse> createVacationRequest(CreateVacationRequest request, UUID staffId) {
        Doctor doctor = _doctorRepository.findOneById(staffId);
        Nurse nurse = _nurseRepository.findOneById(staffId);
        Set<VacationResponse> responses = new HashSet<>();
        if(nurse == null){
            for (Date date: request.getDates()) {
                Schedule schedule = new Schedule();
                schedule.setDate(date);
                schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_VACATION);
                schedule.setApproved(false);
                schedule.setDoctor(doctor);
                _scheduleRepository.save(schedule);
                VacationResponse response = new VacationResponse();
                response.setFirstName(doctor.getUser().getFirstName());
                response.setLastName(doctor.getUser().getLastName());
                response.setDate(date);
                response.setClinicName(doctor.getClinic().getName());
                response.setScheduleId(schedule.getId());
                responses.add(response);
            }

        }

        return responses;
    }

    @Override
    public Set<VacationResponse> getAllVacationRequests() {
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_VACATION);
        return schedules.stream().map(schedule -> mapScheduleToVacationResponse(schedule))
                .collect(Collectors.toSet());
    }

    @Override
    public void approveVacation(UUID id) throws Exception {
        Schedule schedule = _scheduleRepository.findOneById(id);
        if(schedule.getDoctor() != null){
            List<Schedule> examinations = schedule.getDoctor().getSchedules();
            boolean flag = false;
            for (Schedule s: examinations) {
                if(s.getDate().getYear() == schedule.getDate().getYear()
                    && s.getDate().getMonth() == schedule.getDate().getMonth()
                    && s.getDate().getDay() == schedule.getDate().getDay()){
                    flag = true;
                    break;
                }
            }
            if(flag){
                schedule.setReasonOfUnavailability(ReasonOfUnavailability.DENIED_VACATION);
                _scheduleRepository.save(schedule);
                throw new Exception("Doktor ima zakazan pregled i ne moze da uzme godisnji odmor za ovaj dan.");
            }
            schedule.setReasonOfUnavailability(ReasonOfUnavailability.VACATION);
            schedule.setApproved(true);
            _scheduleRepository.save(schedule);
        }

    }

    @Override
    public void denyVacation(UUID id) {
        Schedule schedule = _scheduleRepository.findOneById(id);
        schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_VACATION);
        _scheduleRepository.save(schedule);
    }

    public VacationResponse mapScheduleToVacationResponse(Schedule schedule){
        VacationResponse response = new VacationResponse();
        response.setScheduleId(schedule.getId());
        response.setDate(schedule.getDate());
        if(schedule.getDoctor() != null){
            response.setClinicName(schedule.getDoctor().getClinic().getName());
            response.setFirstName(schedule.getDoctor().getUser().getFirstName());
            response.setLastName(schedule.getDoctor().getUser().getLastName());
        }
        return response;
    }
}
