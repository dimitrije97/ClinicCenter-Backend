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
        Set<ExaminationType> examinationTypes = _examinationTypeRepository.findAllByDeleted(false);
        for (ExaminationType et: examinationTypes){
            if(et.getName().equals(request.getName())){
                throw new Exception("Već postoji tip pregleda sa istim imenom.");
            }
        }
        ExaminationType examinationType = new ExaminationType();
        examinationType.setDeleted(false);
        examinationType.setName(request.getName());
        examinationType.setPrice(request.getPrice());
        ExaminationType savedExaminationType = _examinationTypeRepository.save(examinationType);
        return mapExaminationTypeToExaminationTypeResponse(savedExaminationType);
    }

    @Override
    public ExaminationTypeResponse updateExaminationType(UpdateExaminationRequest request, UUID id) throws Exception {
        Set<ExaminationType> examinationTypes = _examinationTypeRepository.findAllByDeleted(false);
        for (ExaminationType et: examinationTypes){
            if(et.getName().equals(request.getName())){
                throw new Exception("Već postoji tip pregleda sa istim imenom.");
            }
        }
        ExaminationType examinationType = _examinationTypeRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getExaminationType().getId().equals(id)){
                throw new Exception("Postoji zakazan pregled datog tipa.");
            }
        }
        examinationType.setPrice(request.getPrice());
        examinationType.setName(request.getName());
        ExaminationType savedExaminationType = _examinationTypeRepository.save(examinationType);
        return mapExaminationTypeToExaminationTypeResponse(savedExaminationType);
    }

    @Override
    public ExaminationTypeResponse getExaminationType(UUID id) { return mapExaminationTypeToExaminationTypeResponse(_examinationTypeRepository.findOneById(id)); }

    @Override
    public Set<ExaminationTypeResponse> getAllExaminationTypes() throws Exception {
        Set<ExaminationType> examinationTypes = _examinationTypeRepository.findAllByDeleted(false);
        if(examinationTypes.isEmpty()){
            throw new Exception("Ne postoji nijedan tip pregleda.");
        }
        return examinationTypes.stream().map(examinationType -> mapExaminationTypeToExaminationTypeResponse(examinationType))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ExaminationTypeResponse> getAllExaminationTypesOfClinic(UUID clinicId) throws Exception {
        Set<Doctor> doctors = _doctorRepository.findAllByClinic_IdAndUser_Deleted(clinicId, false);
        Set<ExaminationType> examinationTypes = new HashSet<>();
        for (Doctor doctor: doctors) {
            ExaminationType et = doctor.getExaminationType();
            examinationTypes.add(et);
        }
        if(examinationTypes.isEmpty()){
            throw new Exception("Ne postoji nijedan tip pregleda u klinici.");
        }
        return examinationTypes.stream().map(examinationType -> mapExaminationTypeToExaminationTypeResponse(examinationType))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteExaminationType(UUID id) throws Exception {
        ExaminationType examinationType = _examinationTypeRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getExaminationType().getId().equals(id)){
                throw new Exception("Postoji zakazan pregled datog tipa.");
            }
        }
        examinationType.setDeleted(true);
        _examinationTypeRepository.save(examinationType);
        Set<Doctor> doctors = _doctorRepository.findAllByUser_DeletedAndExaminationType(false, examinationType);
        for (Doctor doctor: doctors) {
            doctor.getUser().setDeleted(true);
            _doctorRepository.save(doctor);
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
