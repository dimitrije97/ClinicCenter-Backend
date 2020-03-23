package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateClinicRequest;
import com.example.demo.dto.request.NewClinicAdminRequest;
import com.example.demo.dto.request.UpdateClinicRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Clinic;
import com.example.demo.repository.IAdminRepository;
import com.example.demo.repository.IClinicRepository;
import com.example.demo.service.IClinicService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClinicService implements IClinicService {

    private final IClinicRepository _clinicRepository;

    private final IAdminRepository _adminRepository;

    public ClinicService(IClinicRepository clinicRepository, IAdminRepository adminRepository) {
        _clinicRepository = clinicRepository;
        _adminRepository = adminRepository;
    }

    @Override
    public ClinicResponse createClinic(CreateClinicRequest request) {
        Clinic clinic = new Clinic();
        clinic.setAddress(request.getAddress());
        clinic.setDescription(request.getDescription());
        clinic.setName(request.getName());
        clinic.setDeleted(false);

        Clinic savedClinic = _clinicRepository.save(clinic);

        return mapClinicToClinicResponse(savedClinic);
    }

    @Override
    public ClinicResponse getClinic(UUID id) { return mapClinicToClinicResponse(_clinicRepository.findOneById(id)); }

    @Override
    public Set<ClinicResponse> getAllClinics() {
        Set<Clinic> clinics = _clinicRepository.findAllByDeleted(false);

        return clinics.stream().map(clinic -> mapClinicToClinicResponse(clinic))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteClinic(UUID id) {
        Clinic clinic = _clinicRepository.findOneById(id);
        clinic.setDeleted(true);
        _clinicRepository.save(clinic);
    }

    @Override
    public ClinicResponse updateClinic(UpdateClinicRequest request, UUID id) {
        Clinic clinic = _clinicRepository.findOneById(id);
        clinic.setName(request.getName());
        clinic.setDescription(request.getDescription());
        clinic.setAddress(request.getAddress());
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

    public ClinicResponse mapClinicToClinicResponse(Clinic clinic){
        ClinicResponse clinicResponse = new ClinicResponse();

        clinicResponse.setAddress(clinic.getAddress());
        clinicResponse.setDescription(clinic.getDescription());
        clinicResponse.setName(clinic.getName());
        clinicResponse.setId(clinic.getId());

        return clinicResponse;
    }
}
