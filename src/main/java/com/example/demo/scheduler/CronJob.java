package com.example.demo.scheduler;

import com.example.demo.dto.request.SuggestRequest;
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

//    @Scheduled(cron="0 * * ? * *") //every minute
    @Scheduled(cron="0 0 0 * * ?")
    public void cronJobSchedule() throws Exception {
        Set<Examination> examinations = _examinationRepository.findAllByStatus(RequestType.PENDING);
        for (Examination e: examinations) {
            SuggestRequest request = new SuggestRequest();
            request.setExaminationId(e.getId());
            String startAtString = e.getSchedule().getStartAt().toString();
            String[] startAtTokens = startAtString.split(":");
            String startEndString = e.getSchedule().getEndAt().toString();
            String[] startEndTokens = startEndString.split(":");
            if((Integer.parseInt(startAtTokens[0]) - Integer.parseInt(startEndTokens[0]) != -1)){
                _suggestService.suggestOp(request);
                System.out.println("CRONJOB-OPERATION");
            }else {
                _suggestService.suggest(request);
                System.out.println("CRONJOB-EXAMINATION");
            }
        }
    }
}
