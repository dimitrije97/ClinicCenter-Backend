package com.example.demo.repository;

import com.example.demo.entity.ClinicCenterAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IClinicCenterAdminRepository extends JpaRepository<ClinicCenterAdmin, UUID> {

    ClinicCenterAdmin findOneById(UUID id);

    Set<ClinicCenterAdmin> findAllByUser_Deleted(boolean deleted);
}
