package com.example.demo.service.implementation;

import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IScheduleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements IScheduleService {

    private final IScheduleRepository _scheduleRepository;

    public ScheduleService(IScheduleRepository scheduleRepository) {
        _scheduleRepository = scheduleRepository;
    }

    @Override
    public Set<PatientResponse> getAllPateintsByDoctor(UUID id) {
        List<Schedule> schedules = _scheduleRepository.findAllByDoctor_IdAndApproved(id, true);
        Set<Patient> patients = new HashSet<>();
        for(int i = 0;i < schedules.size();i++) {
            patients.add(schedules.get(i).getPatient());
        }
        return patients.stream().map(patient -> mapPatientToPatientResponse(patient))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DoctorResponse> getAllDoctorsByPatient(UUID id) {
        List<Schedule> schedules = _scheduleRepository.findAllByPatient_IdAndApproved(id, true);
        Set<Doctor> doctors = new HashSet<>();
        for(int i = 0;i < schedules.size();i++) {
            doctors.add(schedules.get(i).getDoctor());
        }
        return doctors.stream().map(doctor -> mapDoctorToDoctorResponse(doctor))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ClinicResponse> getAllClinicsByPatient(UUID id) {
        List<Schedule> schedules = _scheduleRepository.findAllByPatient_IdAndApproved(id, true);
        Set<Clinic> clinics = new HashSet<>();
        for(int i = 0;i < schedules.size();i++) {
            clinics.add(schedules.get(i).getDoctor().getClinic());
        }
        return clinics.stream().map(clinic -> mapClinicToClinicResponse(clinic))
                .collect(Collectors.toSet());
    }

    private PatientResponse mapPatientToPatientResponse(Patient patient) {
        PatientResponse patientResponse = new PatientResponse();
        User user = patient.getUser();
        patientResponse.setEmail(user.getEmail());
        patientResponse.setId(patient.getId());
        patientResponse.setAddress(user.getAddress());
        patientResponse.setCity(user.getCity());
        patientResponse.setCountry(user.getCountry());
        patientResponse.setFirstName(user.getFirstName());
        patientResponse.setLastName(user.getLastName());
        patientResponse.setPhone(user.getPhone());
        patientResponse.setSsn(user.getSsn());
        return patientResponse;
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

    public ClinicResponse mapClinicToClinicResponse(Clinic clinic){
        ClinicResponse clinicResponse = new ClinicResponse();

        clinicResponse.setAddress(clinic.getAddress());
        clinicResponse.setDescription(clinic.getDescription());
        clinicResponse.setName(clinic.getName());
        clinicResponse.setId(clinic.getId());

        return clinicResponse;
    }
}
