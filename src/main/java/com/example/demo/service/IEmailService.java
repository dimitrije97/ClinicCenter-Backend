package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.Patient;


public interface IEmailService {

    void approveRegistrationMail(Patient patient);

    void denyRegistrationMail(Patient patient, String reason);

    void announceAdminsAboutExaminationRequestMail(Admin admin);

    void approveExaminationToPatientMail(Patient patient);

    void denyExaminationToPatientMail(Patient patient, String reason);

    void approveExaminationToDoctorMail(Doctor doctor);

    void denyExaminationToDoctorMail(Doctor doctor, String reason);

    void approveVacationToDoctorMail(Doctor doctor);

    void denyVacationToDoctorMail(Doctor doctor, String reason);

    void approveVacationToNurseMail(Nurse nurse);

    void denyVacationToNurseMail(Nurse nurse, String reason);

    void announceAdminsAboutOperationRequestMail(Admin admin);

    void approveOperationToPatientMail(Patient patient);

    void approveOperationToDoctorMail(Doctor doctor);
}
