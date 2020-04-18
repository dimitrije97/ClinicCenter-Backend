package com.example.demo.service;

import com.example.demo.dto.request.CreateReportRequest;
import com.example.demo.dto.response.ReportResponse;

import java.util.List;

public interface IReportService {

    ReportResponse createReport(CreateReportRequest reportRequest) throws Exception;

    List<ReportResponse> getAllReports() throws Exception;
}
