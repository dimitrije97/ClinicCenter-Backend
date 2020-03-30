package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.ExaminationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, UUID> {

    Doctor findOneById(UUID id);

    Set<Doctor> findAllByUser_Deleted(boolean deleted);

    Set<Doctor> findAllByClinic_IdAndUser_Deleted(UUID clinicId, boolean deleted);

    Set<Doctor> findAllByUser_DeletedAndExaminationType(boolean deleted, ExaminationType examinationType);
}
