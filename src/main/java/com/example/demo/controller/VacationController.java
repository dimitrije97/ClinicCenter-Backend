package com.example.demo.controller;

import com.example.demo.dto.request.CreateVacationRequest;
import com.example.demo.dto.response.VacationResponse;
import com.example.demo.service.IVacationService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("vacations")
public class VacationController {

    private final IVacationService _vacationService;

    public VacationController(IVacationService vacationService) {
        _vacationService = vacationService;
    }

    @PostMapping("/{id}/staff")
    public Set<VacationResponse> createVacationRequest(@RequestBody CreateVacationRequest request, @PathVariable UUID id){
        return _vacationService.createVacationRequest(request, id);
    }

    @GetMapping
    public Set<VacationResponse> getAllVacationRequests(){
        return _vacationService.getAllVacationRequests();
    }

    @GetMapping("/{id}/admin")
    public Set<VacationResponse> getAllVacationRequestsByAdmin(@PathVariable UUID id) throws Exception {
        return _vacationService.getAllVacationRequestsByAdmin(id);
    }

    @PostMapping("/approve/{id}/vacation-request")
    public void approveVacation(@PathVariable UUID id) throws Exception {
        _vacationService.approveVacation(id);
    }

    @DeleteMapping("/deny/{id}/vacation-request")
    public void denyVacation(@PathVariable UUID id){
        _vacationService.denyVacation(id);
    }
}