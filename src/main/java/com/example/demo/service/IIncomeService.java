package com.example.demo.service;

import com.example.demo.dto.request.GetExaminationTypesIncomInClinicRequest;
import com.example.demo.dto.response.IncomeResponse;
import com.example.demo.dto.response.MonthlyIncomeResponse;

import java.util.UUID;

public interface IIncomeService {

    IncomeResponse getClinicsIncome(UUID clinicId) throws Exception;

    IncomeResponse getClinicCentersIncome() throws Exception;

    IncomeResponse getDoctorsIncome(UUID doctorId) throws Exception;

    IncomeResponse getExaminationTypesIncome(UUID examinationTypeId) throws Exception;

    IncomeResponse getExaminationTypesIncomInClinic(GetExaminationTypesIncomInClinicRequest request, UUID clinicId) throws Exception;

    MonthlyIncomeResponse getClinicsMonthlyIncome(UUID clinicId) throws Exception;
}
