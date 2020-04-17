package com.example.demo.controller;

import com.example.demo.dto.request.CreateMedicineRequest;
import com.example.demo.dto.request.UpdateMedicineRequest;
import com.example.demo.dto.response.MedicineResponse;
import com.example.demo.service.IMedicineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PutMapping
    public MedicineResponse updateMedicine(@RequestBody UpdateMedicineRequest request) throws Exception {
        return _medicineService.updateMedicine(request);
    }

    @DeleteMapping("/{id}/medicine")
    public void deleteMedicine(@PathVariable UUID id){
        _medicineService.deleteMedicine(id);
    }

    @GetMapping
    public List<MedicineResponse> getAllMedicines() throws Exception{
        return _medicineService.getAllMedicines();
    }
}
