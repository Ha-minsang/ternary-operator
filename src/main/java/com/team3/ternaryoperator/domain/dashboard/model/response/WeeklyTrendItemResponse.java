package com.team3.ternaryoperator.domain.dashboard.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyTrendItemResponse {
    private String name;
    private long tasks;
    private long completed;
    private String date;
}