package com.example.demo.service;

import com.example.demo.entity.Patient;
import com.example.demo.entity.User;

public interface IEmailService {

    void approveRegistrationMail(Patient patient);

    void denyRegistrationMail(Patient patient, String reason);
}
