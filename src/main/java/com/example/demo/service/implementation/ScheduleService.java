package com.example.demo.service.implementation;

import com.example.demo.dto.response.ScheduleResponse;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.repository.INurseRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements IScheduleService {

    private final IScheduleRepository _scheduleRepository;

    private final IDoctorRepository _doctorRepository;

    private final INurseRepository _nurseRepository;

    public ScheduleService(IScheduleRepository scheduleRepository, IDoctorRepository doctorRepository, INurseRepository nurseRepository) {
        _scheduleRepository = scheduleRepository;
        _doctorRepository = doctorRepository;
        _nurseRepository = nurseRepository;
    }

    @Override
    public List<ScheduleResponse> getAllDoctorsSchedules(UUID id) throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndDoctor(true, _doctorRepository.findOneById(id));
        if(schedules.isEmpty()){
            throw new Exception("Nemate za sada ništa u Vašem radnom kalendaru.");
        }
        return schedules.stream()
                .map(schedule -> mapScheduleToScheduleResponse(schedule))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getAllNursesSchedules(UUID id) throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndNurse(true, _nurseRepository.findOneById(id));
        if(schedules.isEmpty()){
            throw new Exception("Nemate za sada ništa u Vašem radnom kalendaru.");
        }
        return schedules.stream()
                .map(schedule -> mapScheduleToScheduleResponse(schedule))
                .collect(Collectors.toList());
    }

    private ScheduleResponse mapScheduleToScheduleResponse(Schedule schedule){
        ScheduleResponse response = new ScheduleResponse();
        response.setDate(schedule.getDate());
        response.setStartAt(schedule.getStartAt());
        response.setEndAt(schedule.getEndAt());
        response.setReasonOfUnavailability(schedule.getReasonOfUnavailability());
        response.setId(schedule.getId());

        return response;
    }
}
