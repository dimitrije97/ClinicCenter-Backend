package com.example.demo.controller;

import com.example.demo.dto.request.GetExaminationTypesIncomInClinicRequest;
import com.example.demo.dto.response.IncomeResponse;
import com.example.demo.service.IIncomeService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IIncomeService _incomeService;

    public IncomeController(IIncomeService incomeService) {
        _incomeService = incomeService;
    }

    @GetMapping("/{id}/clinic")
    public IncomeResponse getClinicsIncome(@PathVariable UUID id) throws Exception {
        return _incomeService.getClinicsIncome(id);
    }

    @GetMapping()
    public IncomeResponse getClinicCentersIncome() throws Exception {
        return _incomeService.getClinicCentersIncome();
    }

    @GetMapping("/{id}/doctor")
    public IncomeResponse getDoctorsIncome(@PathVariable UUID id) throws Exception {
        return _incomeService.getDoctorsIncome(id);
    }

    @GetMapping("/{id}/examination-type")
    public IncomeResponse getExaminationTypesIncomeInClinicCenter(@PathVariable UUID id) throws Exception {
        return _incomeService.getExaminationTypesIncome(id);
    }

    @GetMapping("/examination-type/{id}/clinic")
    public IncomeResponse getExaminationTypesIncomeInClinic(@RequestBody GetExaminationTypesIncomInClinicRequest request, @PathVariable UUID id) throws Exception {
        return _incomeService.getExaminationTypesIncomInClinic(request, id);
    }
}
