package com.example.demo.repository;

import com.example.demo.entity.EmergencyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IEmergencyRoomRepository extends JpaRepository<EmergencyRoom, UUID> {

    EmergencyRoom findOneById(UUID id);

    Set<EmergencyRoom> findAllByDeleted(boolean deleted);

    Set<EmergencyRoom> findAllByDeletedAndClinic_Id(boolean deleted, UUID clinicId);
}
