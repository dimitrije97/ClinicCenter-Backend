package com.example.demo.service.implementation;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.GradeResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.IGradeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GradeService implements IGradeService {

    private final IDoctorRepository _doctorRepository;

    private final IClinicRepository _clinicRepository;

    private final IPatientRepository _patientRepository;

    private final IScheduleRepository _scheduleRepository;

    private final IGradeRepository _gradeRepository;

    public GradeService(IDoctorRepository doctorRepository, IClinicRepository clinicRepository, IPatientRepository patientRepository, IScheduleRepository scheduleRepository, IGradeRepository gradeRepository) {
        _doctorRepository = doctorRepository;
        _clinicRepository = clinicRepository;
        _patientRepository = patientRepository;
        _scheduleRepository = scheduleRepository;
        _gradeRepository = gradeRepository;
    }

    @Override
    public GradeResponse gradeDoctor(GradeRequest request) throws Exception {
        Patient patient = _patientRepository.findOneById(request.getPatientId());
        List<Grade> grades = patient.getGrades();
        for (Grade grade: grades) {
            if(grade.getDoctor() != null) {
                if (request.getDoctorOrClinicId().equals(grade.getDoctor().getId())) {
                    throw new Exception("Vec ste ocenili ovog lekara.");
                }
            }
        }
        Grade grade = new Grade();
        grade.setDoctor(_doctorRepository.findOneById(request.getDoctorOrClinicId()));
        grade.setPatient(_patientRepository.findOneById(request.getPatientId()));
        grade.setGrade(request.getGrade());
        Grade savedGrade = _gradeRepository.save(grade);
        return mapGradeToGradeResponse(savedGrade);
    }

    @Override
    public GradeResponse gradeClinic(GradeRequest request) throws Exception {
        Patient patient = _patientRepository.findOneById(request.getPatientId());
        List<Grade> grades = patient.getGrades();
        for (Grade grade: grades) {
            if(grade.getClinic() != null) {
                if (request.getDoctorOrClinicId().equals(grade.getClinic().getId())) {
                    throw new Exception("Vec ste ocenili ovu klinuku.");
                }
            }
        }
        Grade grade = new Grade();
        grade.setClinic(_clinicRepository.findOneById(request.getDoctorOrClinicId()));
        grade.setPatient(_patientRepository.findOneById(request.getPatientId()));
        grade.setGrade(request.getGrade());
        Grade savedGrade = _gradeRepository.save(grade);
        return mapGradeToGradeResponse(savedGrade);
    }

    @Override
    public Set<GradeResponse> getAllGradesByDoctor(UUID doctorId) throws Exception {
        Doctor doctor = _doctorRepository.findOneById(doctorId);
        if(doctor.getGrades().isEmpty()){
            throw new Exception("Ne postoji nijednda ocena.");
        }
        return doctor.getGrades().stream().map(grade -> mapGradeToGradeResponse(grade))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<GradeResponse> getAllGradesByClinic(UUID clinicId) throws Exception {
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        if(clinic.getGrades().isEmpty()){
            throw new Exception("Ne postoji nijednda ocena.");
        }
        return clinic.getGrades().stream().map(grade -> mapGradeToGradeResponse(grade))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<GradeResponse> getAllGradedDoctorsPatient(UUID patientId) throws Exception {
        Patient patient = _patientRepository.findOneById(patientId);
        Set<Grade> doctorGrades = new HashSet<>();
        List<Grade> grades = patient.getGrades();
        for (Grade grade: grades) {
            if(grade.getClinic() == null){
                doctorGrades.add(grade);
            }
        }
        if(doctorGrades.isEmpty()){
            throw new Exception("Ne postoji nijedan ocenjeni doktor.");
        }
        return doctorGrades.stream().map(grade -> mapGradeToGradeResponse(grade))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<GradeResponse> getAllGradedClinicsPatient(UUID patientId) throws Exception {
        Patient patient = _patientRepository.findOneById(patientId);
        Set<Grade> clinicGrades = new HashSet<>();
        List<Grade> grades = patient.getGrades();
        for (Grade grade: grades) {
            if(grade.getClinic() == null){
                clinicGrades.add(grade);
            }
        }
        if(clinicGrades.isEmpty()){
            throw new Exception("Ne postoji nijedna ocenjena klinika.");
        }
        return clinicGrades.stream().map(grade -> mapGradeToGradeResponse(grade))
                .collect(Collectors.toSet());
    }

    @Override
    public GradeResponse getAvgGradeByDoctor(UUID doctorId) throws Exception {
        Doctor doctor = _doctorRepository.findOneById(doctorId);
        if(doctor.getGrades().isEmpty()){
            throw new Exception("Ne postoji nijedna ocena.");
        }
        float sum = 0;
        for (Grade grade: doctor.getGrades()) {
            sum += Float.valueOf(grade.getGrade());
        }
        sum = sum / doctor.getGrades().size();
        GradeResponse response = new GradeResponse();
        response.setGrade(String.valueOf(sum));
        return response;
    }

    @Override
    public GradeResponse getAvgGradeByClinic(UUID clinicId) throws Exception {
        Clinic clinic = _clinicRepository.findOneById(clinicId);
        if(clinic.getGrades().isEmpty()){
            throw new Exception("Ne postoji nijedna ocena.");
        }
        float sum = 0;
        for (Grade grade: clinic.getGrades()) {
            sum += Float.valueOf(grade.getGrade());
        }
        sum = sum / clinic.getGrades().size();
        GradeResponse response = new GradeResponse();
        response.setGrade(String.valueOf(sum));
        return response;
    }

    @Override
    public Set<DoctorResponse> getAllDoctorsWhoCanBeGradedByPatient(UUID patientId) throws Exception {
        Patient patient = _patientRepository.findOneById(patientId);
//        Set<Doctor> doctors = patient.getDoctors();

        Set<Doctor> doctors = new HashSet<>();
        Date now = new Date();
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndPatientId(true, patientId);
        for (Schedule s: schedules) {
            if(s.getDate().before(now)){
                doctors.add(s.getDoctor());
            }
        }

        if(doctors.isEmpty()){
            throw new Exception("Niste imali pregled do danas.");
        }
        for (Grade grade: patient.getGrades()) {
            if(doctors.contains(grade.getDoctor()))
                doctors.remove(grade.getDoctor());
        }
        if(doctors.isEmpty()){
            throw new Exception("Vec ste ocenili sve lekare koji su vas pregledali.");
        }
        return doctors.stream().map(doctor -> mapDoctorToDoctorResponse(doctor))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ClinicResponse> getAllClinicsWhichCanBeGradedByPatient(UUID patientId) throws Exception {
        Patient patient = _patientRepository.findOneById(patientId);
//        Set<Doctor> doctors = patient.getDoctors();

        Set<Doctor> doctors = new HashSet<>();
        Date now = new Date();
        List<Schedule> schedules = _scheduleRepository.findAllByApprovedAndPatientId(true, patientId);
        for (Schedule s: schedules) {
            if(s.getDate().before(now)){
                doctors.add(s.getDoctor());
            }
        }

        if(doctors.isEmpty()){
            throw new Exception("Niste imali pregled do danas.");
        }
        Set<Clinic> clinics = new HashSet<>();
        for (Doctor doctor: doctors) {
            clinics.add(doctor.getClinic());
        }
        for (Grade grade: patient.getGrades()) {
            if(clinics.contains(grade.getClinic()))
                clinics.remove(grade.getClinic());
        }
        if(clinics.isEmpty()){
            throw new Exception("Vec ste ocenili sve klinike u kojima ste imali preglede.");
        }
        return clinics.stream().map(clinic -> mapClinicToClinicResponse(clinic))
                .collect(Collectors.toSet());
    }

    public GradeResponse mapGradeToGradeResponse(Grade grade) {
        GradeResponse gradeResponse = new GradeResponse();
        if(grade.getClinic() != null){
            gradeResponse.setClinicName(grade.getClinic().getName());
        }
        if(grade.getDoctor() != null){
            gradeResponse.setDoctorFirstName(grade.getDoctor().getUser().getFirstName());
            gradeResponse.setDoctorLastName(grade.getDoctor().getUser().getLastName());
        }
        gradeResponse.setPatientFirstName(grade.getPatient().getUser().getFirstName());
        gradeResponse.setPatientLastName(grade.getPatient().getUser().getLastName());
        gradeResponse.setGrade(grade.getGrade());
        gradeResponse.setId(grade.getId());
        return gradeResponse;
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
