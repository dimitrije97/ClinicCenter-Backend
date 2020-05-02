package com.example.demo.dto.response;

import lombok.Data;

@Data
public class DailyIncomeResponse {

    private String thisDayIncome;

    private String lastDayIncome;

    private String lastLastDayIncome;

    private String thisDayExaminations;

    private String lastDayExaminations;

    private String lastLastDayExaminations;

    private String thisDayIncomePercent;

    private String lastDayIncomePercent;

    private String lastLastDayIncomePercent;
}
