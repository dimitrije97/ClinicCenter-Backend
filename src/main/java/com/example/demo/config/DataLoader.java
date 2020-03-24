package com.example.demo.config;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
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
    private IDoctorRepository doctorRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private INurseRepository nurseRepository;

    @Autowired
    private IClinicCenterAdminRepository clinicCenterAdminRepository;

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

        ids.add(UUID.fromString("adfa0bd5-c1b5-41d7-adc4-b6951beb9055"));
        ids.add(UUID.fromString("8dbea129-360a-4d77-afca-5c5bb94174c1"));
        ids.add(UUID.fromString("b3fd3799-83ff-4f8d-bc2f-38f0a3980c4a"));
        ids.add(UUID.fromString("9608b8fe-4cff-49ee-8997-2ef2494e21cb"));
        ids.add(UUID.fromString("f130446d-5b2e-4908-a6be-1c0218e15d52"));

        ids.add(UUID.fromString("358359e1-9a6d-4196-95a3-af8fdc294fd4"));
        ids.add(UUID.fromString("767609d0-dd8a-487f-9ef0-a2433b71d49b"));
        ids.add(UUID.fromString("b7b8e9ee-44f2-4722-a0c0-5343e1595f1e"));
        ids.add(UUID.fromString("eb8381fb-fd8d-4e8f-bd31-ed82626230bd"));
        ids.add(UUID.fromString("88a23210-81ff-4088-9b1b-1d235016162f"));

        ids.add(UUID.fromString("d2f71233-b6de-42ba-98ba-5d4befc02efe"));
        ids.add(UUID.fromString("ff453f1b-a5fe-49ee-a22a-be037a55a78a"));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        setupIds();
        for (int i = 0; i < 22; i++) {
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
                if(i != 9) {
                    request.setSsn(String.format("222222222222%s", i + 1));
                } else{
                    request.setSsn(String.format("22222222222%s", i + 1));
                }
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
            }else if(i >= 10 && i < 15){
                Doctor doctor = null;
                doctor = doctorRepository.findOneById(ids.get(i));
                request.setUserType(UserType.DOCTOR);
                request.setEmail(String.format("doctor%s@gmail.com", i + 1));
                request.setSsn(String.format("33333333333%s", i + 1));
                request.setAddress(String.format("Adresa%s", i + 1));
                request.setCity(String.format("Grad%s", i + 1));
                request.setCountry(String.format("Drzava%s", i + 1));
                request.setFirstName(String.format("Ime%s", i + 1));
                request.setLastName(String.format("Prezime%s", i + 1));
                request.setPhone(String.format("Telefon%s", i + 1));
                request.setPassword(String.format("doctor"));
                request.setRePassword(String.format("doctor"));
                UserResponse userResponse = userService.createUser(request);
                User user = new User();
                user.setDeleted(false);
                user.setId(userResponse.getId());
                doctor.setUser(user);
                doctorRepository.save(doctor);
            }else if(i >= 15 && i < 20){
                Nurse nurse = null;
                nurse = nurseRepository.findOneById(ids.get(i));
                request.setUserType(UserType.NURSE);
                request.setEmail(String.format("nurse%s@gmail.com", i + 1));
                request.setSsn(String.format("44444444444%s", i + 1));
                request.setAddress(String.format("Adresa%s", i + 1));
                request.setCity(String.format("Grad%s", i + 1));
                request.setCountry(String.format("Drzava%s", i + 1));
                request.setFirstName(String.format("Ime%s", i + 1));
                request.setLastName(String.format("Prezime%s", i + 1));
                request.setPhone(String.format("Telefon%s", i + 1));
                request.setPassword(String.format("nurse"));
                request.setRePassword(String.format("nurse"));
                UserResponse userResponse = userService.createUser(request);
                User user = new User();
                user.setDeleted(false);
                user.setId(userResponse.getId());
                nurse.setUser(user);
                nurseRepository.save(nurse);
            }else if(i >= 20){
                ClinicCenterAdmin clinicCenterAdmin = null;
                clinicCenterAdmin = clinicCenterAdminRepository.findOneById(ids.get(i));
                request.setUserType(UserType.CLINIC_CENTER_ADMIN);
                request.setEmail(String.format("ccadmin%s@gmail.com", i + 1));
                request.setSsn(String.format("55555555555%s", i + 1));
                request.setAddress(String.format("Adresa%s", i + 1));
                request.setCity(String.format("Grad%s", i + 1));
                request.setCountry(String.format("Drzava%s", i + 1));
                request.setFirstName(String.format("Ime%s", i + 1));
                request.setLastName(String.format("Prezime%s", i + 1));
                request.setPhone(String.format("Telefon%s", i + 1));
                request.setPassword(String.format("ccadmin"));
                request.setRePassword(String.format("ccadmin"));
                UserResponse userResponse = userService.createUser(request);
                User user = new User();
                user.setDeleted(false);
                user.setId(userResponse.getId());
                clinicCenterAdmin.setUser(user);
                clinicCenterAdminRepository.save(clinicCenterAdmin);
            }
        }
    }
}