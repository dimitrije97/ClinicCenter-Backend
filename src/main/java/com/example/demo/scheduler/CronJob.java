package com.example.demo.scheduler;

import com.example.demo.entity.Examination;
import com.example.demo.repository.IExaminationRepository;
import com.example.demo.service.ISuggestService;
import com.example.demo.util.enums.RequestType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CronJob {
    private final IExaminationRepository _examinationRepository;

    private final ISuggestService _suggestService;

    public CronJob(IExaminationRepository examinationRepository, ISuggestService suggestService) {
        _examinationRepository = examinationRepository;
        _suggestService = suggestService;
    }

    @Scheduled(cron="0 0 0 * * ?")
    public void cronJobSchedule() throws Exception {
        Set<Examination> examinations = _examinationRepository.findAllByStatus(RequestType.PENDING);
        for (Examination e: examinations) {
            _suggestService.suggest(e.getId());
            System.out.println("CRONJOB ");
        }
    }
}
