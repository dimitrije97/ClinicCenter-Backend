package com.example.demo.service.implementation;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IAuthService;
import com.example.demo.util.enums.UserType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    private final PasswordEncoder _passwordEncoder;

    private final IUserRepository _userRepository;

    private final IPatientRepository _patientRepository;

    public AuthService(PasswordEncoder passwordEncoder, IUserRepository userRepository,
                       IPatientRepository patientRepository) {
        _passwordEncoder = passwordEncoder;
        _userRepository = userRepository;
        _patientRepository = patientRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        User user = _userRepository.findOneByEmail(request.getUsername());

        if (user == null) {
            throw new Exception(String.format("User with email does not exist"));
        }

        if (!_passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Wrong password");
        }

        if (user.getUserType().equals(UserType.PATIENT)) {
            user.setFirstTimeLoggedIn(new Date());
            _userRepository.save(user);
        }

        UserResponse userResponse = mapUserToUserResponse(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserResponse(userResponse);

        return loginResponse;
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        UUID id = null;
        if (user.getUserType().equals(UserType.PATIENT)) {
            id = user.getPatient().getId();
        }
        userResponse.setId(id);
        userResponse.setAddress(user.getAddress());
        userResponse.setCity(user.getCity());
        userResponse.setCountry(user.getCountry());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setSsn(user.getSsn());
        userResponse.setUserType(user.getUserType());

        // only on login
        userResponse.setSetNewPassword(user.getFirstTimeLoggedIn() == null);

        return userResponse;
    }
}
