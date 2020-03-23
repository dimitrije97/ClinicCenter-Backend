package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.NewPasswordRequest;
import com.example.demo.dto.response.LoginResponse;

import java.util.UUID;

public interface IAuthService {

    LoginResponse login(LoginRequest request) throws Exception;

    LoginResponse setNewPassword(UUID id, NewPasswordRequest request) throws Exception;
}
