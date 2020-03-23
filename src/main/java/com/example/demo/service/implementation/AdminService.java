package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateAdminRequest;
import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateAdminRequest;
import com.example.demo.dto.response.AdminResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import com.example.demo.repository.IAdminRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IAdminService;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {

    private final IUserService _userService;

    private final IUserRepository _userRepository;

    private final IAdminRepository _adminRepository;

    public AdminService(IUserService userService, IUserRepository userRepository, IAdminRepository adminRepository) {
        _userService = userService;
        _userRepository = userRepository;
        _adminRepository = adminRepository;
    }

    @Override
    public AdminResponse createAdmin(CreateAdminRequest request) throws Exception {

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword(request.getPassword());
        userRequest.setRePassword(request.getRePassword());
        userRequest.setAddress(request.getAddress());
        userRequest.setCity(request.getCity());
        userRequest.setEmail(request.getEmail());
        userRequest.setCountry(request.getCountry());
        userRequest.setFirstName(request.getFirstName());
        userRequest.setLastName(request.getLastName());
        userRequest.setSsn(request.getSsn());
        userRequest.setPhone(request.getPhone());
        userRequest.setUserType(UserType.ADMIN);
        UserResponse userResponse = _userService.createUser(userRequest);
        User user = _userRepository.findOneById(userResponse.getId());
        user.setId(userResponse.getId());

        Admin admin = new Admin();
        admin.setUser(user);

        Admin savedAdmin = _adminRepository.save(admin);

        return mapAdminToAdminResponse(savedAdmin);
    }

    @Override
    public AdminResponse getAdmin(UUID id) { return mapAdminToAdminResponse(_adminRepository.findOneById(id)); }

    @Override
    public void deleteAdmin(UUID id) {

        Admin admin = _adminRepository.findOneById(id);
        admin.getUser().setDeleted(true);
        _adminRepository.save(admin);
    }

    @Override
    public AdminResponse updateAdmin(UpdateAdminRequest request, UUID id) {

        Admin admin = _adminRepository.findOneById(id);
        User user = admin.getUser();

        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        Admin savedAdmin = _adminRepository.save(admin);

        return mapAdminToAdminResponse(savedAdmin);
    }

    @Override
    public Set<AdminResponse> getAllAdmins() {

        Set<Admin> admins = _adminRepository.findAllByUser_Deleted(false);

        return admins.stream().map(admin -> mapAdminToAdminResponse(admin))
                .collect(Collectors.toSet());
    }

    private AdminResponse mapAdminToAdminResponse(Admin admin) {
        AdminResponse adminResponse = new AdminResponse();
        User user = admin.getUser();
        adminResponse.setEmail(user.getEmail());
        adminResponse.setId(admin.getId());
        adminResponse.setAddress(user.getAddress());
        adminResponse.setCity(user.getCity());
        adminResponse.setCountry(user.getCountry());
        adminResponse.setFirstName(user.getFirstName());
        adminResponse.setLastName(user.getLastName());
        adminResponse.setPhone(user.getPhone());
        adminResponse.setSsn(user.getSsn());
        return adminResponse;
    }
}
