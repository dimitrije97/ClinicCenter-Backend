package com.example.demo.service.implementation;

import com.example.demo.config.EmailContext;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Nurse;
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
        String subject = "Vaša registracija je odobrena!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        context.setVariable("link", String.format("http://localhost:4200/auth/login/%s/patient", patient.getId()));
        _emailContext.send(to, subject, "approvedRegistration", context);
    }

    @Override
    public void denyRegistrationMail(Patient patient, String reason) {
        String to = patient.getUser().getEmail();
        String subject = "Vaša registracija je odbijena!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedRegistration", context);
    }

    @Override
    public void announceAdminsAboutExaminationRequestMail(Admin admin) {
        String to = admin.getUser().getEmail();
        String subject = "Novi zahtev za pregled!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", admin.getUser().getFirstName(), admin.getUser().getLastName()));
        _emailContext.send(to, subject, "examinationRequest", context);
    }

    @Override
    public void approveExaminationToPatientMail(Patient patient) {
        String to = patient.getUser().getEmail();
        String subject = "Vaš zahtev za pregled je odobren!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        _emailContext.send(to, subject, "approvedExaminationToPatient", context);
    }

    @Override
    public void denyExaminationToPatientMail(Patient patient, String reason) {
        String to = patient.getUser().getEmail();
        String subject = "Novi zahtev za pregled je odbijen!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedExaminationToPatient", context);
    }

    @Override
    public void approveExaminationToDoctorMail(Doctor doctor) {
        String to = doctor.getUser().getEmail();
        String subject = "Novi zahtev za pregled je odobren!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", doctor.getUser().getFirstName(), doctor.getUser().getLastName()));
        _emailContext.send(to, subject, "approvedExaminationToDoctor", context);
    }

    @Override
    public void denyExaminationToDoctorMail(Doctor doctor, String reason) {
        String to = doctor.getUser().getEmail();
        String subject = "Novi zahtev za pregled je odbijen!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", doctor.getUser().getFirstName(), doctor.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedExaminationToDoctor", context);
    }

    @Override
    public void approveVacationToDoctorMail(Doctor doctor) {
        String to = doctor.getUser().getEmail();
        String subject = "Vaš zahtev za godišnji odmor je odobren!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", doctor.getUser().getFirstName(), doctor.getUser().getLastName()));
        _emailContext.send(to, subject, "approvedVacation", context);
    }

    @Override
    public void denyVacationToDoctorMail(Doctor doctor, String reason) {
        String to = doctor.getUser().getEmail();
        String subject = "Vaš zahtev za godišnji odmor je odbijen!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", doctor.getUser().getFirstName(), doctor.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedVacation", context);
    }

    @Override
    public void approveVacationToNurseMail(Nurse nurse) {
        String to = nurse.getUser().getEmail();
        String subject = "Vaš zahtev za godišnji odmor je odobren!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", nurse.getUser().getFirstName(), nurse.getUser().getLastName()));
        _emailContext.send(to, subject, "approvedVacation", context);
    }

    @Override
    public void denyVacationToNurseMail(Nurse nurse, String reason) {
        String to = nurse.getUser().getEmail();
        String subject = "Vaš zahtev za godišnji odmor je odbijen!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", nurse.getUser().getFirstName(), nurse.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedVacation", context);
    }

    @Override
    public void announceAdminsAboutOperationRequestMail(Admin admin) {
        String to = admin.getUser().getEmail();
        String subject = "Novi zahtev za operaciju!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", admin.getUser().getFirstName(), admin.getUser().getLastName()));
        _emailContext.send(to, subject, "operationRequest", context);
    }

    @Override
    public void approveOperationToPatientMail(Patient patient) {
        String to = patient.getUser().getEmail();
        String subject = "Imate zakazanu operaciju!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        _emailContext.send(to, subject, "approvedExaminationToPatient", context);
    }

    @Override
    public void approveOperationToDoctorMail(Doctor doctor) {
        String to = doctor.getUser().getEmail();
        String subject = "Novi zahtev za operaciju je odobren!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", doctor.getUser().getFirstName(), doctor.getUser().getLastName()));
        _emailContext.send(to, subject, "approvedOperationToDoctor", context);
    }

    @Override
    public void denyOperationToPatientMail(Patient patient, String reason) {
        String to = patient.getUser().getEmail();
        String subject = "Novi zahtev za operaciju je odbijen!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", patient.getUser().getFirstName(), patient.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedOperationToPatient", context);
    }

    @Override
    public void denyOperationToDoctorMail(Doctor doctor, String reason) {
        String to = doctor.getUser().getEmail();
        String subject = "Novi zahtev za operaciju je odbijen!";
        Context context = new Context();
        context.setVariable("name", String.format("%s %s", doctor.getUser().getFirstName(), doctor.getUser().getLastName()));
        context.setVariable("reason", String.format("%s", reason));
        _emailContext.send(to, subject, "deniedOperationToDoctor", context);
    }
}
