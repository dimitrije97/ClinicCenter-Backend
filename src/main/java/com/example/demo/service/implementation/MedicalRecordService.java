package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateMedicalRecordRequest;
import com.example.demo.dto.request.GetMedicalRecordRequest;
import com.example.demo.dto.request.UpdateMedicalRecordRequest;
import com.example.demo.dto.response.MedicalRecordResponse;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.MedicalRecord;
import com.example.demo.entity.Patient;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.repository.IMedicalRecordReposiroty;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.service.IMedicalRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService implements IMedicalRecordService {

    private final IMedicalRecordReposiroty _medicalRecordReposiroty;

    private final IPatientRepository _patientRepository;

    private final IDoctorRepository _doctorRepository;

    public MedicalRecordService(IMedicalRecordReposiroty medicalRecordReposiroty, IPatientRepository patientRepository, IDoctorRepository doctorRepository) {
        _medicalRecordReposiroty = medicalRecordReposiroty;
        _patientRepository = patientRepository;
        _doctorRepository = doctorRepository;
    }

    @Override
    public MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest request) throws Exception {
        if(request.getPatientId() == null){
            throw new Exception("Niste izabrali pacijenta.");
        }
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setAllergy(request.getAllergy());
        medicalRecord.setHeight(request.getHeight());
        medicalRecord.setWeight(request.getWeight());
        medicalRecord.setDiopter(request.getDiopter());
        Patient patient = _patientRepository.findOneById(request.getPatientId());
        if(patient.getMedicalRecord() != null){
            throw new Exception("Pacijent već poseduje zdravstveni karton.");
        }
        medicalRecord.setPatient(patient);
        patient.setMedicalRecord(medicalRecord);
//        _patientRepository.save(patient);
        MedicalRecord savedMedicalRecord = _medicalRecordReposiroty.save(medicalRecord);
        return mapMedicalRecordToMedicalRecordResponse(savedMedicalRecord);
    }

    @Override
    public List<MedicalRecordResponse> getAllMedicalRecords() throws Exception {
        List<MedicalRecord> records = _medicalRecordReposiroty.findAll();
        if(records.isEmpty()){
            throw new Exception("Ne postoji nijedan zdravstveni karton.");
        }
        return records.stream()
                .map(medicalRecord -> mapMedicalRecordToMedicalRecordResponse(medicalRecord))
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordResponse getMedicalRecordByPatient(GetMedicalRecordRequest request) throws Exception {
        Doctor doctor = _doctorRepository.findOneById(request.getDoctorId());
        if(doctor != null){
            Patient patient = _patientRepository.findOneById(request.getPatientId());
            if(!patient.getDoctors().contains(doctor)){
                throw new Exception("Možete videti zdravstveni karton samo onih pacijenata koji su kod Vas imali pregled.");
            }
        }

        MedicalRecord medicalRecord = _medicalRecordReposiroty.findOneByPatient_Id(request.getPatientId());
        if(medicalRecord == null){
            throw new Exception("Zdravstveni karton nije napravljen.");
        }
        return mapMedicalRecordToMedicalRecordResponse(medicalRecord);
    }

    @Override
    public MedicalRecordResponse updateMedicalRecord(UpdateMedicalRecordRequest request) throws Exception {
        Doctor doctor = _doctorRepository.findOneById(request.getDoctorId());
        Patient patient = _patientRepository.findOneByUser_Email(request.getPatientEmail());
        Date now = new Date();
        LocalTime currentTime = request.getCurrentTime();
        boolean flag =  doctor.getSchedules().stream()
                .anyMatch(schedule -> schedule.getDate().getYear() == now.getYear()
                        && schedule.getDate().getMonth() == now.getMonth()
                        && schedule.getDate().getDay() == now.getDay()
                        && schedule.getStartAt().isBefore(currentTime)
                        && schedule.getEndAt().isAfter(currentTime)
                        && schedule.getPatient().getId().equals(patient.getId()));

        if(!flag) {
            throw new Exception("Trenutno ne vršite pregled ovog pacijenta.");
        }

        MedicalRecord medicalRecord = patient.getMedicalRecord();
        medicalRecord.setDiopter(request.getDiopter());
        medicalRecord.setWeight(request.getWeight());
        medicalRecord.setHeight(request.getHeight());
        medicalRecord.setAllergy(request.getAllergy());
        MedicalRecord savedMedicalRecprd = _medicalRecordReposiroty.save(medicalRecord);
        return mapMedicalRecordToMedicalRecordResponse(savedMedicalRecprd);
    }


    public MedicalRecordResponse mapMedicalRecordToMedicalRecordResponse(MedicalRecord medicalRecord){
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setAllergy(medicalRecord.getAllergy());
        response.setHeight(medicalRecord.getHeight());
        response.setWeight(medicalRecord.getWeight());
        response.setDiopter(medicalRecord.getDiopter());
        response.setPatientEmail(medicalRecord.getPatient().getUser().getEmail());
        response.setPatientName(medicalRecord.getPatient().getUser().getFirstName());
        response.setPatientSurname(medicalRecord.getPatient().getUser().getLastName());
        response.setId(medicalRecord.getId());
        return response;
    }
}
