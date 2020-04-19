package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateMedicalRecordRequest;
import com.example.demo.dto.response.MedicalRecordResponse;
import com.example.demo.entity.MedicalRecord;
import com.example.demo.repository.IMedicalRecordReposiroty;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.service.IMedicalRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService implements IMedicalRecordService {

    private final IMedicalRecordReposiroty _medicalRecordReposiroty;

    private final IPatientRepository _patientRepository;

    public MedicalRecordService(IMedicalRecordReposiroty medicalRecordReposiroty, IPatientRepository patientRepository) {
        _medicalRecordReposiroty = medicalRecordReposiroty;
        _patientRepository = patientRepository;
    }

    @Override
    public MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest request) throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setAllergy(request.getAllergy());
        medicalRecord.setHeight(request.getHeight());
        medicalRecord.setWeight(request.getWeight());
        medicalRecord.setPatient(_patientRepository.findOneById(request.getPatientId()));
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
    public MedicalRecordResponse getMedicalRecordByPatient(UUID id) throws Exception {
        MedicalRecord medicalRecord = _medicalRecordReposiroty.findOneByPatient_Id(id);
        if(medicalRecord == null){
            throw new Exception("Zdravstveni karton nije napravljen.");
        }
        return mapMedicalRecordToMedicalRecordResponse(medicalRecord);
    }


    public MedicalRecordResponse mapMedicalRecordToMedicalRecordResponse(MedicalRecord medicalRecord){
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setAllergy(medicalRecord.getAllergy());
        response.setHeight(medicalRecord.getHeight());
        response.setWeight(medicalRecord.getWeight());
        response.setPatientEmail(medicalRecord.getPatient().getUser().getEmail());
        response.setPatientName(medicalRecord.getPatient().getUser().getFirstName());
        response.setPatientSurname(medicalRecord.getPatient().getUser().getLastName());
        response.setId(medicalRecord.getId());
        return response;
    }
}
