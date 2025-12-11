package com.team3.ternaryoperator.domain.dashboard.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WeeklyTrendResponse {
    private List<WeeklyTrendItemResponse> data;
}