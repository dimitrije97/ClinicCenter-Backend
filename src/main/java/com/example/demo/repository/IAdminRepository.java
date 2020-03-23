package com.example.demo.repository;

import com.example.demo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, UUID> {

    Admin findOneById(UUID id);

    Set<Admin> findAllByUser_Deleted(boolean deleted);

    Set<Admin> findAllByClinic_IdAndUser_Deleted(UUID clinicId, boolean deleted);

}
