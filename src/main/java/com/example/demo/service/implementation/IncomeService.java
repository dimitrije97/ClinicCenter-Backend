package com.example.demo.service.implementation;

import com.example.demo.dto.request.GetExaminationTypesIncomInClinicRequest;
import com.example.demo.dto.response.IncomeResponse;
import com.example.demo.entity.Clinic;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.ExaminationType;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.repository.IExaminationTypeRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IIncomeService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.UUID;

@Service
public class IncomeService implements IIncomeService {

    private final IClinicRepository _clinicRepository;

    private final IScheduleRepository _scheduleRepository;

    private final IDoctorRepository _doctorRepository;

    private final IExaminationTypeRepository _examinationTypeRepository;

    public IncomeService(IClinicRepository clinicRepository, IScheduleRepository scheduleRepository, IDoctorRepository doctorRepository, IExaminationTypeRepository examinationTypeRepository) {
        _clinicRepository = clinicRepository;
        _scheduleRepository = scheduleRepository;
        _doctorRepository = doctorRepository;
        _examinationTypeRepository = examinationTypeRepository;
    }

    @Override
    public IncomeResponse getClinicsIncome(UUID id) throws Exception {
        Clinic clinic = _clinicRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        float income = 0, number = 0;
        for (Schedule schedule: schedules) {
            if(clinic.getDoctors().contains(schedule.getDoctor())){
                income += Float.valueOf(schedule.getDoctor().getExaminationType().getPrice());
                number++;
            }
        }
        if(income == 0){
            throw new Exception("Klinika nije imala prihode.");
        }
        IncomeResponse response = new IncomeResponse();
        response.setExaminationsNumber(String.valueOf(number));
        response.setIncome(String.valueOf(income));
        return response;
    }

    @Override
    public IncomeResponse getClinicCentersIncome() throws Exception {
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        float income = 0, number = 0;
        for (Schedule schedule: schedules) {
            income += Float.valueOf(schedule.getDoctor().getExaminationType().getPrice());
            number++;
        }
        if(income == 0){
            throw new Exception("Klinički centar nije imao prihode.");
        }

        IncomeResponse response = new IncomeResponse();
        response.setExaminationsNumber(String.valueOf(number));
        response.setIncome(String.valueOf(income));
        return response;
    }

    @Override
    public IncomeResponse getDoctorsIncome(UUID id) throws Exception {
        Doctor doctor = _doctorRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        float income = 0, number = 0;
        for (Schedule schedule: schedules) {
            if(doctor == schedule.getDoctor()){
                income += Float.valueOf(schedule.getDoctor().getExaminationType().getPrice());
                number++;
            }
        }
        if(income == 0){
            throw new Exception("Doktor nije imao prihode.");
        }

        IncomeResponse response = new IncomeResponse();
        response.setExaminationsNumber(String.valueOf(number));
        response.setIncome(String.valueOf(income));
        return response;
    }

    @Override
    public IncomeResponse getExaminationTypesIncome(UUID id) throws Exception {
        ExaminationType examinationType = _examinationTypeRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        float income = 0, number = 0;
        for (Schedule schedule: schedules) {
            if(examinationType == schedule.getDoctor().getExaminationType()){
                income += Float.valueOf(schedule.getDoctor().getExaminationType().getPrice());
                number++;
            }
        }
        if(income == 0){
            throw new Exception("Za ovaj tip pregleda klinički centar nije imao prihode.");
        }

        IncomeResponse response = new IncomeResponse();
        response.setExaminationsNumber(String.valueOf(number));
        response.setIncome(String.valueOf(income));
        return response;
    }

    @Override
    public IncomeResponse getExaminationTypesIncomInClinic(GetExaminationTypesIncomInClinicRequest request, UUID clinicId) throws Exception {
        ExaminationType examinationType = _examinationTypeRepository.findOneById(request.getExamintaionTypeId());
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        float income = 0, number = 0;
        for (Schedule schedule: schedules) {
            if(examinationType == schedule.getDoctor().getExaminationType() && clinic.getDoctors().contains(schedule.getDoctor())){
                income += Float.valueOf(schedule.getDoctor().getExaminationType().getPrice());
                number ++;
            }
        }
        if(income == 0){
            throw new Exception("Za ovaj tip pregleda klinika nije imala prihode.");
        }

        IncomeResponse response = new IncomeResponse();
        response.setExaminationsNumber(String.valueOf(number));
        response.setIncome(String.valueOf(income));
        return response;
    }
}
