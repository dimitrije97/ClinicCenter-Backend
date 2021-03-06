package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.Schedule;
import com.example.demo.util.enums.ReasonOfUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, UUID> {

    Schedule findOneById(UUID id);

    List<Schedule> findAllByReasonOfUnavailability(ReasonOfUnavailability reasonOfUnavailability);

    List<Schedule> findAllByApprovedAndNurse(boolean approved, Nurse nurse);

    List<Schedule> findAllByApprovedAndPatientId(boolean approved, UUID patientId);

    List<Schedule> findAllByApprovedAndDoctor(boolean approved, Doctor doctor);

    List<Schedule> findAllByApprovedAndDoctorAndDate(boolean approved, Doctor doctor, Date date);

    List<Schedule> findAllByApprovedAndDate(boolean approved, Date date);
}
