package com.example.demo.repository;

import com.example.demo.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface INurseRepository extends JpaRepository<Nurse, UUID> {

    Nurse findOneById(UUID id);

    Set<Nurse> findAllByUser_Deleted(boolean deleted);

    Set<Nurse> findAllByUser_DeletedAndClinic_Id(boolean deleted, UUID id);
}
