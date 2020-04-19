package com.example.demo.repository;

import com.example.demo.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IMedicalRecordReposiroty extends JpaRepository<MedicalRecord, UUID> {

    MedicalRecord findOneById(UUID id);

    MedicalRecord findOneByPatient_Id(UUID id);
}
