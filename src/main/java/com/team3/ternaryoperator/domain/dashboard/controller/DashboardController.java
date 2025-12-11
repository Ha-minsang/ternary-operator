package com.team3.ternaryoperator.domain.dashboard.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.*;
import com.team3.ternaryoperator.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<CommonResponse<DashboardStatsResponse>> getDashboardStats(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        DashboardStatsResponse response = dashboardService.getDashboardStats(authUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "대시보드 통계 조회 성공"));
    }

    @GetMapping("/weekly-trend")
    public ResponseEntity<CommonResponse<WeeklyTrendResponse>> getWeeklyTrend() {

        List<WeeklyTrendItemResponse> result = dashboardService.getWeeklyTrend();

        WeeklyTrendResponse response = new WeeklyTrendResponse(result);

        return ResponseEntity.ok(CommonResponse.success(response, "주간 작업 추세 조회 성공"));
    }

    @GetMapping("/tasks")
    public ResponseEntity<CommonResponse<MyTaskSummaryResponse>> getMyTaskSummary(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MyTaskSummaryResponse summary = dashboardService.getMyTaskSummary(authUser);
        return ResponseEntity.ok(
                CommonResponse.success(summary, "내 작업 요약 조회 성공")
        );
    }
}
