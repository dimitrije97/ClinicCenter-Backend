package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateReportRequest;
import com.example.demo.dto.response.ReportResponse;
import com.example.demo.entity.Examination;
import com.example.demo.entity.MedicalRecord;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.Report;
import com.example.demo.repository.IExaminationRepository;
import com.example.demo.repository.IMedicalRecordReposiroty;
import com.example.demo.repository.IRecipeRepository;
import com.example.demo.repository.IReportRepository;
import com.example.demo.service.IReportService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {

    private final IReportRepository _reportRepository;

    private final IRecipeRepository _recipeRepository;

    private final IExaminationRepository _examinationRepository;

    private final IMedicalRecordReposiroty _medicalRecordReposiroty;

    public ReportService(IReportRepository reportRepository, IRecipeRepository recipeRepository, IExaminationRepository examinationRepository, IMedicalRecordReposiroty medicalRecordReposiroty) {
        _reportRepository = reportRepository;
        _recipeRepository = recipeRepository;
        _examinationRepository = examinationRepository;
        _medicalRecordReposiroty = medicalRecordReposiroty;
    }

    @Override
    public ReportResponse createReport(CreateReportRequest reportRequest) throws Exception {
        Report report = new Report();

        Examination examination = _examinationRepository.findOneById(reportRequest.getExaminationId());
        LocalTime currentTime = reportRequest.getCurrentTime();
        Date now = new Date();
        boolean flag = false;
        if(now.getYear() == examination.getSchedule().getDate().getYear()
        && now.getMonth() == examination.getSchedule().getDate().getMonth()
        && now.getDay() == examination.getSchedule().getDate().getDay()
        && examination.getSchedule().getStartAt().isBefore(currentTime)
        && examination.getSchedule().getEndAt().isAfter(currentTime)){
            if(reportRequest.getExaminationId() != null && !reportRequest.getExaminationId().equals(examination.getSchedule().getDoctor().getId())){
                throw new Exception("Trenutno ne vršite pregled ovog pacijenta.");
            }
            flag = true;
        }

        if(!flag) {
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
            if(mr.getPatient().getId().equals(examination.getSchedule().getPatient().getId())){
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
