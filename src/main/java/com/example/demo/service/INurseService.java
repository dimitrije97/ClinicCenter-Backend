package com.example.demo.service;

import com.example.demo.dto.request.CreateNurseRequest;
import com.example.demo.dto.request.UpdateNurseRequest;
import com.example.demo.dto.response.NurseResponse;

import java.util.Set;
import java.util.UUID;

public interface INurseService {

    NurseResponse createNurse(CreateNurseRequest request, UUID clinicId) throws Exception;

    NurseResponse updateNurse(UpdateNurseRequest request, UUID id) throws Exception;

    NurseResponse getNurse(UUID id);

    Set<NurseResponse> getAllNurses() throws Exception;

    Set<NurseResponse> getAllNursesOfClinic(UUID clinicId) throws Exception;

    void deleteNurse(UUID id);
}
