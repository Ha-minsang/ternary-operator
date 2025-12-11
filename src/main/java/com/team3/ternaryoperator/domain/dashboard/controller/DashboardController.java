package com.team3.ternaryoperator.domain.dashboard.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendItemResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendResponse;
import com.team3.ternaryoperator.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/weekly-trend")
    public ResponseEntity<CommonResponse<WeeklyTrendResponse>> getWeeklyTrend() {

        List<WeeklyTrendItemResponse> result = dashboardService.getWeeklyTrend();

        WeeklyTrendResponse response = new WeeklyTrendResponse(result);

        return ResponseEntity.ok(CommonResponse.success(response, "주간 작업 추세 조회 성공"));
    }

}
