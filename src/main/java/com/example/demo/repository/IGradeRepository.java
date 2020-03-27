package com.example.demo.repository;

import com.example.demo.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IGradeRepository extends JpaRepository<Grade, UUID> {

    Grade findOneById(UUID id);
}
