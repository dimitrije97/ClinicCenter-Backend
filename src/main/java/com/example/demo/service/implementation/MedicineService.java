package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateMedicineRequest;
import com.example.demo.dto.request.UpdateMedicineRequest;
import com.example.demo.dto.response.MedicineResponse;
import com.example.demo.entity.Medicine;
import com.example.demo.repository.IMedicineRepository;
import com.example.demo.service.IMedicineService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MedicineService implements IMedicineService {

    private final IMedicineRepository _medicineRepository;

    public MedicineService(IMedicineRepository medicineRepository) {
        _medicineRepository = medicineRepository;
    }

    @Override
    public MedicineResponse createMedicine(CreateMedicineRequest request) throws Exception {
        Medicine medicine = new Medicine();
        medicine.setName(request.getName());
        medicine.setDeleted(false);
        Medicine savedMedicine = _medicineRepository.save(medicine);
        return mapMedicineToMedicineResponse(savedMedicine);
    }

    @Override
    public MedicineResponse updateMedicine(UpdateMedicineRequest request) throws Exception {
        Medicine medicine = _medicineRepository.findOneById(request.getId());
        medicine.setName(request.getName());
        Medicine savedMedicine = _medicineRepository.save(medicine);
        return mapMedicineToMedicineResponse(savedMedicine);
    }

    @Override
    public void deleteMedicine(UUID id) {
        Medicine medicine = _medicineRepository.findOneById(id);
        medicine.setDeleted(true);
        _medicineRepository.save(medicine);
    }


    public MedicineResponse mapMedicineToMedicineResponse(Medicine medicine){
        MedicineResponse response = new MedicineResponse();
        response.setId(medicine.getId());
        response.setName(medicine.getName());
        return response;
    }
}
