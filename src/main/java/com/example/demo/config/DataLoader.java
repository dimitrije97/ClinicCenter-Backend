package com.example.demo.config;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.repository.IAdminRepository;
import com.example.demo.repository.IPatientRepository;
import com.example.demo.service.IUserService;
import com.example.demo.util.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DataLoader implements ApplicationRunner {

    private List<UUID> ids = new ArrayList();

    @Autowired
    private IPatientRepository patientRepository;

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private IUserService userService;

    private void setupIds() {
        ids.add(UUID.fromString("e14cf27c-cd22-4004-b0f7-c94fcd13aabf"));
        ids.add(UUID.fromString("170dccaf-cf4d-4e9e-aa4e-1e3498d17a97"));
        ids.add(UUID.fromString("d1b5fb6a-0bdd-4df1-8fda-aef7560d7828"));
        ids.add(UUID.fromString("7086dad5-bc13-4977-a7ff-a36d58e068cf"));
        ids.add(UUID.fromString("fa9594c4-430f-420e-a7f0-a3055b7b9aa3"));

        ids.add(UUID.fromString("bbce973b-7ef1-4bf3-99f0-1b87771b3075"));
        ids.add(UUID.fromString("d719558f-2126-48b0-b76a-3e0bb3c10048"));
        ids.add(UUID.fromString("a82468e6-8913-48dd-b842-4be4e37aeb35"));
        ids.add(UUID.fromString("f9fda70a-7761-43f7-9fe6-00046bb12a02"));
        ids.add(UUID.fromString("4e58f698-f022-4eee-95ab-810b5ffdcd99"));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        setupIds();
        for (int i = 0; i < 10; i++) {
            CreateUserRequest request = new CreateUserRequest();
            if (i < 5) {
                Patient patient = null;
                patient = patientRepository.findOneById(ids.get(i));
                request.setUserType(UserType.PATIENT);
                request.setEmail(String.format("pacijent%s@gmail.com", i + 1));
                request.setSsn(String.format("111111111111%s", i + 1));
                request.setAddress(String.format("Adresa%s", i + 1));
                request.setCity(String.format("Grad%s", i + 1));
                request.setCountry(String.format("Drzava%s", i + 1));
                request.setFirstName(String.format("Ime%s", i + 1));
                request.setLastName(String.format("Prezime%s", i + 1));
                request.setPhone(String.format("Telefon%s", i + 1));
                request.setPassword(String.format("sifra%s", i + 1));
                request.setRePassword(String.format("sifra%s", i + 1));
                UserResponse userResponse = userService.createUser(request);
                User user = new User();
                user.setDeleted(false);
                user.setId(userResponse.getId());
                patient.setUser(user);
                patientRepository.save(patient);
            }else if(i >= 5 && i < 10){
                Admin admin = null;
                admin = adminRepository.findOneById(ids.get(i));
                request.setUserType(UserType.ADMIN);
                request.setEmail(String.format("admin%s@gmail.com", i + 1));
                request.setSsn(String.format("222222222222%s", i + 1));
                request.setAddress(String.format("Adresa%s", i + 1));
                request.setCity(String.format("Grad%s", i + 1));
                request.setCountry(String.format("Drzava%s", i + 1));
                request.setFirstName(String.format("Ime%s", i + 1));
                request.setLastName(String.format("Prezime%s", i + 1));
                request.setPhone(String.format("Telefon%s", i + 1));
                request.setPassword(String.format("admin"));
                request.setRePassword(String.format("admin"));
                UserResponse userResponse = userService.createUser(request);
                User user = new User();
                user.setDeleted(false);
                user.setId(userResponse.getId());
                admin.setUser(user);
                adminRepository.save(admin);
            }
        }
    }
}