package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyIncomeResponse {

    private String income;

    private String thisMonthIncome;

    private String lastMonthIncome;

    private String lastLastMonthIncome;

    private String examinations;

    private String thisMonthExaminations;

    private String lastMonthExaminations;

    private String lastLastMonthExaminations;

    private String thisMonthIncomePercent;

    private String lastMonthIncomePercent;

    private String lastLastMonthIncomePercent;
}
