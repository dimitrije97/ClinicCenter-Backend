package com.example.demo.repository;

import com.example.demo.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IDiagnosisRepository extends JpaRepository<Diagnosis, UUID> {

    Diagnosis findOneById(UUID id);

    List<Diagnosis> findAllByDeleted(boolean deleted);
}
