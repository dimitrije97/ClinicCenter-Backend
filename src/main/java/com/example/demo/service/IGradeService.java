package com.example.demo.service;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.response.ClinicResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.GradeResponse;

import java.util.Set;
import java.util.UUID;

public interface IGradeService {

    GradeResponse gradeDoctor(GradeRequest request) throws Exception;

    GradeResponse gradeClinic(GradeRequest request) throws Exception;

    Set<GradeResponse> getAllGradesByDoctor(UUID doctorId) throws Exception;

    Set<GradeResponse> getAllGradesByClinic(UUID clinicId) throws Exception;

    Set<GradeResponse> getAllGradedDoctorsPatient(UUID patientId) throws Exception;

    Set<GradeResponse> getAllGradedClinicsPatient(UUID patientId) throws Exception;

    String getAvgGradeByDoctor(UUID doctorId) throws Exception;

    String getAvgGradeByClinic(UUID clinicId) throws Exception;

    Set<DoctorResponse> getAllDoctorsWhoCanBeGradedByPatient(UUID patientId) throws Exception;

    Set<ClinicResponse> getAllClinicsWhichCanBeGradedByPatient(UUID patientId) throws Exception;
}
