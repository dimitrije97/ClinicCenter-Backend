package com.example.demo.service;

import com.example.demo.dto.request.GetExaminationTypesIncomInClinicRequest;

import java.util.UUID;

public interface IIncomeService {

    String getClinicsIncome(UUID clinicId) throws Exception;

    String getClinicCentersIncome() throws Exception;

    String getDoctorsIncome(UUID doctorId) throws Exception;

    String getExaminationTypesIncome(UUID examinationTypeId) throws Exception;

    String getExaminationTypesIncomInClinic(GetExaminationTypesIncomInClinicRequest request, UUID clinicId) throws Exception;
}
