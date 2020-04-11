package com.example.demo.controller;

import com.example.demo.dto.response.ScheduleResponse;
import com.example.demo.service.IScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("schedules")
public class ScheduleController {

    private final IScheduleService _scheduleService;

    public ScheduleController(IScheduleService scheduleService) {
        _scheduleService = scheduleService;
    }

    @GetMapping("/{id}/doctor")
    public List<ScheduleResponse> getAllDoctorsSchedules(@PathVariable UUID id) throws Exception{
        return _scheduleService.getAllDoctorsSchedules(id);
    }

    @GetMapping("/{id}/nurse")
    public List<ScheduleResponse> getAllNursesSchedules(@PathVariable UUID id) throws Exception{
        return _scheduleService.getAllNursesSchedules(id);
    }
}
