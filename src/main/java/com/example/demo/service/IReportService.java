package com.example.demo.service;

import com.example.demo.dto.request.CreateReportRequest;
import com.example.demo.dto.response.ReportResponse;

import java.util.List;
import java.util.UUID;

public interface IReportService {

    ReportResponse createReport(CreateReportRequest reportRequest) throws Exception;

    List<ReportResponse> getAllReports() throws Exception;

    List<ReportResponse> getAllReportsByMedicalRecord(UUID id) throws Exception;
}
