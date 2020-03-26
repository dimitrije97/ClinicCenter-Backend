package com.example.demo.repository;

import com.example.demo.entity.Patient;
import com.example.demo.util.enums.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, UUID> {

    Patient findOneById(UUID id);

    Patient findOneByUser_Id(UUID id);

    Set<Patient> findAllByRequestTypeAndUser_Deleted(RequestType requestType, boolean deleted);

    Set<Patient> findAllByRequestType(RequestType requestType);

    Patient findOneByUser_Email(String username);
}
