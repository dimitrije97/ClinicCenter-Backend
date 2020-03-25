package com.example.demo.repository;

import com.example.demo.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, UUID> {

    Schedule findOneById(UUID id);

    List<Schedule> findAllByDoctor_IdAndApproved(UUID id, boolean approved);

    List<Schedule> findAllByPatient_IdAndApproved(UUID id, boolean approved);
}
