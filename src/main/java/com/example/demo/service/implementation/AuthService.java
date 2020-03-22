package com.example.demo.service.implementation;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IAuthService;
import com.example.demo.util.enums.RequestType;
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
            throw new Exception(String.format("Ne postoji korisnik sa datim emailom."));
        }

        if (!_passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Pogresna lozinka.");
        }

        if(user.isDeleted()){
            throw new Exception("Vas nalog je obrisan.");
        }

        if(user.getUserType().equals(UserType.PATIENT)){
            Patient patient = _patientRepository.findOneByUser_Id(user.getId());
            if(patient.getRequestType().equals(RequestType.PENDING)){
                throw new Exception("Vas nalog i dalje nije prihvacen od strane administratora klinickog centra.");
            }else if(patient.getRequestType().equals(RequestType.DENIED)){
                throw new Exception("Vas nalog je odbijen od strane administratora klinickog centra.");
            }

            if(user.getFirstTimeLoggedIn() == null){
                user.setFirstTimeLoggedIn(new Date());
                _userRepository.save(user);
            }
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
