package com.example.demo.service.implementation;

import com.example.demo.config.EmailContext;
import com.example.demo.entity.Patient;
import com.example.demo.service.IEmailService;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class EmailService implements IEmailService {

    private final EmailContext _emailContext;

    public EmailService(EmailContext emailContext) {
        _emailContext = emailContext;
    }

    @Override
    public void approveRegistrationMail(Patient patient) {
        String to = patient.getUser().getEmail();
        String subject = "Vasa registracija je odobrena!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        context.setVariable("link", String.format("%s", patient.getId()));
        _emailContext.send(to, subject, "approvedRegistration", context);
    }

    @Override
    public void denyRegistrationMail(Patient patient, String reason) {
        String to = patient.getUser().getEmail();
        String subject = "Vasa registracija je odbijena!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedRegistration", context);
    }
}
