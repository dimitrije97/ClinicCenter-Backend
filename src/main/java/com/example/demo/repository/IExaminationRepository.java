package com.example.demo.repository;

import com.example.demo.entity.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IExaminationRepository extends JpaRepository<Examination, UUID> {

    Examination findOneById(UUID id);
}
