package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateDiagnosisRequest;
import com.example.demo.dto.request.UpdateDiagnosisRequest;
import com.example.demo.dto.response.DiagnosisResponse;
import com.example.demo.entity.Diagnosis;
import com.example.demo.repository.IDiagnosisRepository;
import com.example.demo.service.IDiagnosisService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiagnosisService implements IDiagnosisService {

    private final IDiagnosisRepository _diagnosisRepository;

    public DiagnosisService(IDiagnosisRepository diagnosisRepository) {
        _diagnosisRepository = diagnosisRepository;
    }

    @Override
    public DiagnosisResponse createDiagnosis(CreateDiagnosisRequest request) throws Exception {
        List<Diagnosis> diagnoses = _diagnosisRepository.findAllByDeleted(false);
        for (Diagnosis d: diagnoses){
            if(d.getName().equals(request.getName())){
                throw new Exception("Već postoji dijagnoza sa istim imenom.");
            }
        }
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setName(request.getName());
        diagnosis.setDeleted(false);
        Diagnosis savedDiagnosis = _diagnosisRepository.save(diagnosis);
        return mapDiagnosisToDiagnosisResponse(savedDiagnosis);
    }

    @Override
    public DiagnosisResponse updateDiagnosis(UpdateDiagnosisRequest request) throws Exception {
        List<Diagnosis> diagnoses = _diagnosisRepository.findAllByDeleted(false);
//        for (Diagnosis d: diagnoses){
//            if(d.getName().equals(request.getName())){
//                throw new Exception("Već postoji dijagnoza sa istim imenom.");
//            }
//        }
        Diagnosis diagnosis = _diagnosisRepository.findOneById(request.getId());
        diagnosis.setName(request.getName());
        Diagnosis savedDiagnosis = _diagnosisRepository.save(diagnosis);
        return mapDiagnosisToDiagnosisResponse(savedDiagnosis);
    }

    @Override
    public void deleteDiagnosis(UUID id) {
        Diagnosis diagnosis = _diagnosisRepository.findOneById(id);
        diagnosis.setDeleted(true);
        _diagnosisRepository.save(diagnosis);
    }

    @Override
    public List<DiagnosisResponse> getAllDiagnosis() throws Exception {
        List<Diagnosis> diagnoses = _diagnosisRepository.findAllByDeleted(false);
        if(diagnoses.isEmpty()){
            throw new Exception("Ne postoji nijedna dijagnoza.");
        }
        return diagnoses.stream()
                .map(diagnosis -> mapDiagnosisToDiagnosisResponse(diagnosis))
                .collect(Collectors.toList());
    }

    public DiagnosisResponse mapDiagnosisToDiagnosisResponse(Diagnosis diagnosis){
        DiagnosisResponse response = new DiagnosisResponse();
        response.setId(diagnosis.getId());
        response.setName(diagnosis.getName());
        return response;
    }
}
