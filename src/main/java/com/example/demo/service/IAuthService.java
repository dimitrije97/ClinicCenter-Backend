package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.LoginResponse;

public interface IAuthService {

    LoginResponse login(LoginRequest request) throws Exception;
}
