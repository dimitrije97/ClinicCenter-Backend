package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateVacationRequest;
import com.example.demo.dto.request.DenyVacationRequest;
import com.example.demo.dto.response.VacationResponse;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.Schedule;
import com.example.demo.repository.IAdminRepository;
import com.example.demo.repository.IDoctorRepository;
import com.example.demo.repository.INurseRepository;
import com.example.demo.repository.IScheduleRepository;
import com.example.demo.service.IEmailService;
import com.example.demo.service.IVacationService;
import com.example.demo.util.enums.ReasonOfUnavailability;
import com.example.demo.util.enums.RequestType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VacationService implements IVacationService {

    private final IScheduleRepository _scheduleRepository;

    private final IDoctorRepository _doctorRepository;

    private final INurseRepository _nurseRepository;

    private final IAdminRepository _adminRepository;

    private final IEmailService _emailService;

    public VacationService(IScheduleRepository scheduleRepository, IDoctorRepository doctorRepository, INurseRepository nurseRepository, IAdminRepository adminRepository, IEmailService emailService) {
        _scheduleRepository = scheduleRepository;
        _doctorRepository = doctorRepository;
        _nurseRepository = nurseRepository;
        _adminRepository = adminRepository;
        _emailService = emailService;
    }

    @Override
    public Set<VacationResponse> createVacationRequest(CreateVacationRequest request, UUID staffId) throws Exception {

        if(request.getDates().isEmpty()){
            throw new Exception("Pogrešno ste uneli datume.");
        }

        Doctor doctor = _doctorRepository.findOneById(staffId);
        Nurse nurse = _nurseRepository.findOneById(staffId);
        Set<VacationResponse> responses = new HashSet<>();
        Date now = new Date();
        if(nurse == null){
            for (Date date: request.getDates()) {
                if(date.before(now)){
                    throw new Exception("Datum koji ste izabrali je prošao.");
                }
                Schedule schedule = new Schedule();
                schedule.setDate(date);
                schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_VACATION);
                schedule.setApproved(false);
                schedule.setDoctor(doctor);
                _scheduleRepository.save(schedule);
                VacationResponse response = new VacationResponse();
                response.setFirstName(doctor.getUser().getFirstName());
                response.setLastName(doctor.getUser().getLastName());
                response.setDate(date);
                response.setClinicName(doctor.getClinic().getName());
                response.setScheduleId(schedule.getId());
                responses.add(response);
            }
        }else if(doctor == null){
            for (Date date: request.getDates()) {
                Schedule schedule = new Schedule();
                schedule.setDate(date);
                schedule.setReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_VACATION);
                schedule.setApproved(false);
                schedule.setNurse(nurse);
                _scheduleRepository.save(schedule);
                VacationResponse response = new VacationResponse();
                response.setFirstName(nurse.getUser().getFirstName());
                response.setLastName(nurse.getUser().getLastName());
                response.setDate(date);
                response.setClinicName(nurse.getClinic().getName());
                response.setScheduleId(schedule.getId());
                responses.add(response);
            }
        }

        return responses;
    }

    @Override
    public Set<VacationResponse> getAllVacationRequests() {
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_VACATION);
        return schedules.stream().map(schedule -> mapScheduleToVacationResponse(schedule))
                .collect(Collectors.toSet());
    }

    @Override
    public void approveVacation(UUID id) throws Exception {
        Schedule schedule = _scheduleRepository.findOneById(id);

        Date now = new Date();
        if(schedule.getDate().before(now)){
            throw new Exception("Datum je prošao.");
        }

        if(schedule.getNurse() == null){
            if(schedule.getDoctor().getUser().isDeleted()){
                schedule.setReasonOfUnavailability(ReasonOfUnavailability.DENIED_VACATION);
                _scheduleRepository.save(schedule);
                throw new Exception("Doktor je u medjuvremenu obrisan.");
            }
            List<Schedule> examinations = schedule.getDoctor().getSchedules();
            boolean flag = false;
            for (Schedule s: examinations) {
                if(s.getDate().getYear() == schedule.getDate().getYear()
                    && s.getDate().getMonth() == schedule.getDate().getMonth()
                    && s.getDate().getDay() == schedule.getDate().getDay()
                    && s.getReasonOfUnavailability().equals(ReasonOfUnavailability.EXAMINATION)){
                    flag = true;
                    break;
                }
            }
            if(flag){
                schedule.setReasonOfUnavailability(ReasonOfUnavailability.DENIED_VACATION);
                _scheduleRepository.save(schedule);
                _emailService.denyVacationToDoctorMail(schedule.getDoctor(), "Doktor ima zakazan pregled i ne može da uzme godišnji odmor za ovaj dan.");
                throw new Exception("Doktor ima zakazan pregled i ne može da uzme godišnji odmor za ovaj dan.");
            }
            schedule.setReasonOfUnavailability(ReasonOfUnavailability.VACATION);
            schedule.setApproved(true);
            _scheduleRepository.save(schedule);
            _emailService.approveVacationToDoctorMail(schedule.getDoctor());
        }else if(schedule.getDoctor() == null){
            if(schedule.getNurse().getUser().isDeleted()){
                schedule.setReasonOfUnavailability(ReasonOfUnavailability.DENIED_VACATION);
                _scheduleRepository.save(schedule);
                throw new Exception("Medicinska sestra je u medjuvremenu obrisana.");
            }
            schedule.setReasonOfUnavailability(ReasonOfUnavailability.VACATION);
            schedule.setApproved(true);
            _scheduleRepository.save(schedule);
            _emailService.approveVacationToNurseMail(schedule.getNurse());
        }

    }

    @Override
    public void denyVacation(UUID id, DenyVacationRequest request) {
        Schedule schedule = _scheduleRepository.findOneById(id);
        schedule.setReasonOfUnavailability(ReasonOfUnavailability.DENIED_VACATION);
        _scheduleRepository.save(schedule);
        if(schedule.getNurse() == null){
            _emailService.denyVacationToDoctorMail(schedule.getDoctor(), request.getReason());
        }else if(schedule.getDoctor() == null){
            _emailService.denyVacationToNurseMail(schedule.getNurse(), request.getReason());
        }
    }

    @Override
    public Set<VacationResponse> getAllVacationRequestsByAdmin(UUID adminId) throws Exception {
        Admin admin = _adminRepository.findOneById(adminId);
        List<Schedule> schedules = _scheduleRepository.findAllByReasonOfUnavailability(ReasonOfUnavailability.POTENTIAL_VACATION);

        Set<Schedule> adminSchedules = new HashSet<>();
        for (Schedule s: schedules) {
            if(s.getNurse() == null){
                if(s.getDoctor().getClinic().getAdmins().contains(admin)){
                    adminSchedules.add(s);
                }
            }else if(s.getDoctor() == null){
                if(s.getNurse().getClinic().getAdmins().contains(admin)){
                    adminSchedules.add(s);
                }
            }
        }

        if(adminSchedules.isEmpty()){
            throw new Exception("Nemate novih zahteva za godišnji odmor.");
        }

        return adminSchedules.stream().map(schedule -> mapScheduleToVacationResponse(schedule))
                .collect(Collectors.toSet());
    }

    public VacationResponse mapScheduleToVacationResponse(Schedule schedule){
        VacationResponse response = new VacationResponse();
        response.setScheduleId(schedule.getId());
        response.setDate(schedule.getDate());
        response.setId(schedule.getId());
        if(schedule.getNurse() == null){
            response.setClinicName(schedule.getDoctor().getClinic().getName());
            response.setFirstName(schedule.getDoctor().getUser().getFirstName());
            response.setLastName(schedule.getDoctor().getUser().getLastName());
        }else if(schedule.getDoctor() == null){
            response.setClinicName(schedule.getNurse().getClinic().getName());
            response.setFirstName(schedule.getNurse().getUser().getFirstName());
            response.setLastName(schedule.getNurse().getUser().getLastName());
        }
        return response;
    }
}
