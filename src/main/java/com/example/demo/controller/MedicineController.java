package com.example.demo.controller;

import com.example.demo.dto.request.CreateMedicineRequest;
import com.example.demo.dto.response.MedicineResponse;
import com.example.demo.service.IMedicineService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicines")
public class MedicineController {

    private final IMedicineService _medicineService;

    public MedicineController(IMedicineService medicineService) {
        _medicineService = medicineService;
    }

    @PostMapping
    public MedicineResponse createMedicine(@RequestBody CreateMedicineRequest request) throws Exception {
        return _medicineService.createMedicine(request);
    }
}
