package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final IUserRepository _userRepository;

    private final PasswordEncoder _passwordEncoder;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        _userRepository = userRepository;
        _passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) throws Exception{
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new Exception("Lozinke se ne podudaraju.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCountry(request.getCountry());
        user.setCity(request.getCity());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setSsn(request.getSsn());
        user.setUserType(request.getUserType());
        List<User> users = _userRepository.findAllByDeleted(false);
        user.setDeleted(false);

        for (User u: users) {
            if(u.getEmail().equals(request.getEmail())){
                throw new Exception("Uneli ste već postojeću e-mail adresu.");
            }else if(u.getSsn().equals(request.getSsn())){
                throw new Exception("Uneli ste već postojeći jmbg.");
            }
        }

        user.setPassword(_passwordEncoder.encode(request.getPassword()));

        User savedUser = _userRepository.save(user);

        return mapUserToUserResponse(savedUser);
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setAddress(user.getAddress());
        userResponse.setCity(user.getCity());
        userResponse.setCountry(user.getCountry());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setSsn(user.getSsn());
        userResponse.setUserType(user.getUserType());
        return userResponse;
    }
}
