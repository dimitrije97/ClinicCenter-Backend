package com.example.demo.service;

import com.example.demo.dto.request.CreateVacationRequest;
import com.example.demo.dto.response.VacationResponse;

import java.util.Set;
import java.util.UUID;

public interface IVacationService {

    Set<VacationResponse> createVacationRequest(CreateVacationRequest request, UUID id);

    Set<VacationResponse> getAllVacationRequests();

    void approveVacation(UUID id) throws Exception;

    void denyVacation(UUID id);

    Set<VacationResponse> getAllVacationRequestsByAdmin(UUID adminId) throws Exception;
}
