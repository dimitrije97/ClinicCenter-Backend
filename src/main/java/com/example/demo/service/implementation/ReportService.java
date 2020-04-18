package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateReportRequest;
import com.example.demo.dto.response.ReportResponse;
import com.example.demo.entity.Report;
import com.example.demo.repository.IRecipeRepository;
import com.example.demo.repository.IReportRepository;
import com.example.demo.service.IReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {

    private final IReportRepository _reportRepository;

    private final IRecipeRepository _recipeRepository;

    public ReportService(IReportRepository reportRepository, IRecipeRepository recipeRepository) {
        _reportRepository = reportRepository;
        _recipeRepository = recipeRepository;
    }

    @Override
    public ReportResponse createReport(CreateReportRequest reportRequest) throws Exception {
        Report report = new Report();
        report.setDescription(reportRequest.getDescription());
        report.setRecipe(_recipeRepository.findOneById(reportRequest.getRecipeId()));
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

    public ReportResponse mapReportToReportResponse(Report report){
        ReportResponse response = new ReportResponse();
        response.setDescription(report.getDescription());
        response.setDiagnosisName(report.getRecipe().getDiagnosis().getName());
        response.setMedicineName(report.getRecipe().getMedicine().getName());
        response.setId(report.getId());
        return response;
    }
}
