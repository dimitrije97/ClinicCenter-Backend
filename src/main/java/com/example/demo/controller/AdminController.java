package com.example.demo.controller;

import com.example.demo.dto.request.CreateAdminRequest;
import com.example.demo.dto.request.UpdateAdminRequest;
import com.example.demo.dto.response.AdminResponse;
import com.example.demo.repository.IAdminRepository;
import com.example.demo.service.IAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final IAdminService _adminService;

    private final IAdminRepository _adminRepository;

    public AdminController(IAdminService adminService, IAdminRepository adminRepository) {
        _adminService = adminService;
        _adminRepository = adminRepository;
    }

    @GetMapping("/{id}/admin")
    public AdminResponse getAdmin(@PathVariable UUID id) {
        return _adminService.getAdmin(id);
    }

    @GetMapping()
    public Set<AdminResponse> getAllAdmins() {
        return _adminService.getAllAdmins();
    }

    @GetMapping("/{id}/clinic")
    public Set<AdminResponse> getAllAdminsOfClinic(@PathVariable UUID id) { return _adminService.getAllAdminsOfClinic(id); }

    @DeleteMapping("/{id}/admin")
    public void deleteAdmin(@PathVariable UUID id) {
        _adminService.deleteAdmin(id);
    }

    @PutMapping("/{id}/admin")
    public AdminResponse updateAdmin(@RequestBody UpdateAdminRequest request, @PathVariable UUID id) {
        return _adminService.updateAdmin(request, id);
    }
}
