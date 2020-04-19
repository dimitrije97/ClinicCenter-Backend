package com.example.demo.controller;

import com.example.demo.dto.request.CreateReportRequest;
import com.example.demo.dto.response.ReportResponse;
import com.example.demo.service.IReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final IReportService _reportService;

    public ReportController(IReportService reportService) {
        _reportService = reportService;
    }

    @PostMapping
    public ReportResponse createReport(@RequestBody CreateReportRequest reportRequest) throws Exception {
        return _reportService.createReport(reportRequest);
    }

    @GetMapping
    public List<ReportResponse> getAllReports() throws Exception {
        return _reportService.getAllReports();
    }

    @GetMapping("/{id}/medical-record")
    public List<ReportResponse> getAllReportsByMedicalRecord(@PathVariable  UUID id) throws Exception {
        return _reportService.getAllReportsByMedicalRecord(id);
    }
}
