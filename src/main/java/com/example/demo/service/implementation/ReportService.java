package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateReportRequest;
import com.example.demo.dto.response.ReportResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.IReportService;
import com.example.demo.util.enums.RequestType;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {

    private final IReportRepository _reportRepository;

    private final IRecipeRepository _recipeRepository;

    private final IExaminationRepository _examinationRepository;

    private final IMedicalRecordReposiroty _medicalRecordReposiroty;

    private final IPatientRepository _patientRepository;

    public ReportService(IReportRepository reportRepository, IRecipeRepository recipeRepository, IExaminationRepository examinationRepository, IMedicalRecordReposiroty medicalRecordReposiroty, IPatientRepository patientRepository) {
        _reportRepository = reportRepository;
        _recipeRepository = recipeRepository;
        _examinationRepository = examinationRepository;
        _medicalRecordReposiroty = medicalRecordReposiroty;
        _patientRepository = patientRepository;
    }

    @Override
    public ReportResponse createReport(CreateReportRequest reportRequest) throws Exception {
        Report report = new Report();

        Patient patient = _patientRepository.findOneById(reportRequest.getPatientId());
        LocalTime currentTime = reportRequest.getCurrentTime();
        Date now = new Date();
        boolean flag = true;
        Set<Examination> allExaminations = _examinationRepository.findAllByStatus(RequestType.APPROVED);
        Examination examination = null;
        for(Examination e: allExaminations){
            if(now.getYear() == e.getSchedule().getDate().getYear()
                    && now.getMonth() == e.getSchedule().getDate().getMonth()
                    && now.getDay() == e.getSchedule().getDate().getDay()
                    && e.getSchedule().getStartAt().isBefore(currentTime)
                    && e.getSchedule().getEndAt().isAfter(currentTime)){
                if(e.getSchedule().getDoctor() != null && e.getSchedule().getDoctor().getId().equals(reportRequest.getDoctorId())
                        && e.getSchedule().getPatient().getId().equals(reportRequest.getPatientId())){
                    examination = e;
                    flag = false;
                    break;
                }
            }
        }
        if(flag) {
            throw new Exception("Trenutno ne vršite pregled ovog pacijenta.");
        }


        report.setDescription(reportRequest.getDescription());
        Recipe recipe = _recipeRepository.findOneById(reportRequest.getRecipeId());
        recipe.setWaiting(true);
        report.setRecipe(recipe);
        report.setExamination(examination);
        examination.getReports().add(report);
        List<MedicalRecord> records = _medicalRecordReposiroty.findAll();
        for (MedicalRecord mr: records) {
            if(mr.getPatient().getId().equals(reportRequest.getPatientId())){
                report.setMedicalRecord(mr);
                mr.getReports().add(report);
                break;
            }
        }
        MedicalRecord medicalRecord = _medicalRecordReposiroty.findOneByPatient_Id(examination.getSchedule().getPatient().getId());
        if(medicalRecord != null){
            medicalRecord.getReports().add(report);
        }
        Report savedReport = _reportRepository.save(report);
        return mapReportToReportResponse(savedReport);
    }

    @Override
    public List<ReportResponse> getAllReports() throws Exception {
        List<Report> reports = _reportRepository.findAll();
        if(reports.isEmpty()){
            throw new Exception("Ne postoji nijedan izvestaj.");
        }
        return reports.stream()
                .map(report -> mapReportToReportResponse(report))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReportsByMedicalRecord(UUID id) throws Exception {
        List<Report> reports = _reportRepository.findAllByMedicalRecord_Id(id);
        return reports.stream()
                .map(report -> mapReportToReportResponse(report))
                .collect(Collectors.toList());
    }

    public ReportResponse mapReportToReportResponse(Report report){
        ReportResponse response = new ReportResponse();
        response.setDescription(report.getDescription());
        response.setDiagnosisName(report.getRecipe().getDiagnosis().getName());
        response.setMedicineName(report.getRecipe().getMedicine().getName());
        response.setId(report.getId());
        return response;
    }
}
