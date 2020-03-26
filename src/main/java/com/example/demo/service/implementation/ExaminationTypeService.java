package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateExaminationTypeRequest;
import com.example.demo.dto.request.UpdateExaminationRequest;
import com.example.demo.dto.response.ExaminationTypeResponse;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.ExaminationType;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.repository.IExaminationTypeRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IExaminationTypeService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExaminationTypeService implements IExaminationTypeService {

    private final IExaminationTypeRepository _examinationTypeRepository;

    private final IDoctorRepository _doctorRepository;

    private final IClinicRepository _clinicRepository;

    private final IScheduleRepository _scheduleRepository;

    public ExaminationTypeService(IExaminationTypeRepository examinationTypeRepository, IDoctorRepository doctorRepository, IClinicRepository clinicRepository, IScheduleRepository scheduleRepository) {
        _examinationTypeRepository = examinationTypeRepository;
        _doctorRepository = doctorRepository;
        _clinicRepository = clinicRepository;
        _scheduleRepository = scheduleRepository;
    }

    @Override
    public ExaminationTypeResponse createExaminationType(CreateExaminationTypeRequest request) throws Exception {
        ExaminationType examinationType = new ExaminationType();
        examinationType.setDeleted(false);
        examinationType.setName(request.getName());
        examinationType.setPrice(request.getPrice());
        ExaminationType savedExaminationType = _examinationTypeRepository.save(examinationType);
        return mapExaminationTypeToExaminationTypeResponse(savedExaminationType);
    }

    @Override
    public ExaminationTypeResponse updateExaminationType(UpdateExaminationRequest request, UUID id) throws Exception {
        ExaminationType examinationType = _examinationTypeRepository.findOneById(id);
        examinationType.setPrice(request.getPrice());
        examinationType.setName(request.getName());
        ExaminationType savedExaminationType = _examinationTypeRepository.save(examinationType);
        return mapExaminationTypeToExaminationTypeResponse(savedExaminationType);
    }

    @Override
    public ExaminationTypeResponse getExaminationType(UUID id) { return mapExaminationTypeToExaminationTypeResponse(_examinationTypeRepository.findOneById(id)); }

    @Override
    public Set<ExaminationTypeResponse> getAllExaminationTypes() {
        Set<ExaminationType> examinationTypes = _examinationTypeRepository.findAllByDeleted(false);
        return examinationTypes.stream().map(examinationType -> mapExaminationTypeToExaminationTypeResponse(examinationType))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationTypeResponse> getAllExaminationTypesOfClinic(UUID clinicId) {
        Set<Doctor> doctors = _doctorRepository.findAllByClinic_IdAndUser_Deleted(clinicId, false);
        List<Doctor> doctorList = new ArrayList<Doctor>(doctors);
        Set<ExaminationType> examinationTypes = new HashSet<>();
        for(int i = 0; i < doctors.size();i++){
            ExaminationType et = doctorList.get(i).getExaminationType();
            examinationTypes.add(et);
        }
        return examinationTypes.stream().map(examinationType -> mapExaminationTypeToExaminationTypeResponse(examinationType))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteExaminationType(UUID id) {
        ExaminationType examinationType = _examinationTypeRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getExaminationType().getId().equals(id)){
                flag = true;
                break;
            }
        }
        if(!flag){
            examinationType.setDeleted(true);
            _examinationTypeRepository.save(examinationType);
        }
    }

    public ExaminationTypeResponse mapExaminationTypeToExaminationTypeResponse(ExaminationType examinationType){
        ExaminationTypeResponse examinationTypeResponse = new ExaminationTypeResponse();
        examinationTypeResponse.setId(examinationType.getId());
        examinationTypeResponse.setName(examinationType.getName());
        examinationTypeResponse.setPrice(examinationType.getPrice());
        return examinationTypeResponse;
    }
}
