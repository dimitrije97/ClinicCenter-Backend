package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateClinicRequest;
import com.example.demo.dto.request.NewClinicAdminRequest;
import com.example.demo.dto.request.SearchClinicsRequest;
import com.example.demo.dto.request.UpdateClinicRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Clinic;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IAdminRepository;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IClinicService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClinicService implements IClinicService {

    private final IClinicRepository _clinicRepository;

    private final IAdminRepository _adminRepository;

    private final IScheduleRepository _scheduleRepository;

    public ClinicService(IClinicRepository clinicRepository, IAdminRepository adminRepository, IScheduleRepository scheduleRepository) {
        _clinicRepository = clinicRepository;
        _adminRepository = adminRepository;
        _scheduleRepository = scheduleRepository;
    }

    @Override
    public ClinicResponse createClinic(CreateClinicRequest request) throws Exception{
        Set<Clinic> clinics = _clinicRepository.findAllByDeleted(false);
        for (Clinic c: clinics){
            if(c.getName().equals(request.getName())){
                throw new Exception("Već postoji klinika sa istim imenom.");
            }
        }

        Clinic clinic = new Clinic();
        clinic.setAddress(request.getAddress());
        clinic.setDescription(request.getDescription());
        clinic.setName(request.getName());
        clinic.setDeleted(false);
        try{
            Float.parseFloat(request.getLat());
            clinic.setLat(request.getLat());
        }catch (Exception var5){
            clinic.setLat(null);
        }

        try{
            Float.parseFloat(request.getLon());
            clinic.setLat(request.getLon());
        }catch (Exception var5){
            clinic.setLon(null);
        }

        Clinic savedClinic = _clinicRepository.save(clinic);

        return mapClinicToClinicResponse(savedClinic);
    }

    @Override
    public ClinicResponse getClinic(UUID id) { return mapClinicToClinicResponse(_clinicRepository.findOneById(id)); }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Set<ClinicResponse> getAllClinics() throws Exception {
        Set<Clinic> clinics = _clinicRepository.findAllByDeleted(false);

        if(clinics.isEmpty()){
            throw new Exception("Ne postoji nijedna klinika.");
        }

        return clinics.stream().map(clinic -> mapClinicToClinicResponse(clinic))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteClinic(UUID id) throws Exception{
        Clinic clinic = _clinicRepository.findOneById(id);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.EXAMINATION);
        boolean flag = false;
        for(int i = 0;i < schedules.size();i++){
            if(schedules.get(i).getDoctor().getClinic().getId().equals(id)){
                flag = true;
                break;
            }
        }
        if(flag){
            throw new Exception("Postoji zakazan pregled u klinici.");
        }
        clinic.setDeleted(true);
        _clinicRepository.save(clinic);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ClinicResponse updateClinic(UpdateClinicRequest request, UUID id) throws Exception {
        Set<Clinic> clinics = _clinicRepository.findAllByDeleted(false);
        for (Clinic c: clinics){
            if(c.getName().equals(request.getName())){
                throw new Exception("Već postoji klinika sa istim imenom.");
            }
        }
        Clinic clinic = _clinicRepository.findOneById(id);
        clinic.setName(request.getName());
        clinic.setDescription(request.getDescription());
        clinic.setAddress(request.getAddress());
        try{
            Float.parseFloat(request.getLat());
            clinic.setLat(request.getLat());
        }catch (Exception var5){
            clinic.setLat(null);
        }

        try{
            Float.parseFloat(request.getLon());
            clinic.setLat(request.getLon());
        }catch (Exception var5){
            clinic.setLon(null);
        }
        Clinic savedClinic = _clinicRepository.save(clinic);

        return mapClinicToClinicResponse(savedClinic);
    }

    @Override
    public void addNewClinicAdmin(NewClinicAdminRequest request) {
        Clinic clinic = _clinicRepository.findOneById(request.getClinicId());
        Admin newAdmin = _adminRepository.findOneById(request.getAdminId());
        clinic.getAdmins().add(newAdmin);
        _clinicRepository.save(clinic);
    }

    @Override
    public Set<ClinicResponse> getAllClinics(SearchClinicsRequest request) throws Exception {
        Set<Clinic> clinics = _clinicRepository.findAllByDeleted(false);
        Set<Clinic> searchedClinicsByName = new HashSet<>();
        Set<Clinic> searchedClinicsByNameAndAddress = new HashSet<>();

        for(Clinic clinic: clinics){
            if(clinic.getName().toLowerCase().contains(request.getName().toLowerCase())){
                searchedClinicsByName.add(clinic);
            }
        }

        for(Clinic clinic: searchedClinicsByName){
            if(clinic.getAddress().toLowerCase().contains(request.getAddress().toLowerCase())){
                searchedClinicsByNameAndAddress.add(clinic);
            }
        }

        return searchedClinicsByNameAndAddress.stream()
                .map(clinic -> mapClinicToClinicResponse(clinic))
                .collect(Collectors.toSet());
    }

    public ClinicResponse mapClinicToClinicResponse(Clinic clinic){
        ClinicResponse clinicResponse = new ClinicResponse();

        clinicResponse.setAddress(clinic.getAddress());
        clinicResponse.setDescription(clinic.getDescription());
        clinicResponse.setName(clinic.getName());
        clinicResponse.setId(clinic.getId());
        clinicResponse.setLat(clinic.getLat());
        clinicResponse.setLon(clinic.getLon());

        return clinicResponse;
    }
}
