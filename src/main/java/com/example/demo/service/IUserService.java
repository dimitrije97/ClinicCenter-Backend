package com.example.demo.service;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.response.UserResponse;

public interface IUserService {

    UserResponse createUser(CreateUserRequest request) throws Exception;
}
