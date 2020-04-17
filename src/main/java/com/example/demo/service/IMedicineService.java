package com.example.demo.service;

import com.example.demo.dto.request.CreateMedicineRequest;
import com.example.demo.dto.request.UpdateMedicineRequest;
import com.example.demo.dto.response.MedicineResponse;

import java.util.UUID;

public interface IMedicineService {

    MedicineResponse createMedicine(CreateMedicineRequest request) throws Exception;

    MedicineResponse updateMedicine(UpdateMedicineRequest request) throws Exception;

    void deleteMedicine(UUID id);
}
