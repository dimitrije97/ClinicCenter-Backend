package com.example.demo.repository;

import com.example.demo.entity.ExaminationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IExaminationTypeRepository extends JpaRepository<ExaminationType, UUID> {

    ExaminationType findOneById(UUID id);

    Set<ExaminationType> findAllByDeleted(boolean deleted);
}
