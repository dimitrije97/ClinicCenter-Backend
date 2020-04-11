package com.example.demo.service;

import com.example.demo.dto.response.ScheduleResponse;

import java.util.List;
import java.util.UUID;

public interface IScheduleService {

    List<ScheduleResponse> getAllDoctorsSchedules(UUID id) throws Exception;
}
