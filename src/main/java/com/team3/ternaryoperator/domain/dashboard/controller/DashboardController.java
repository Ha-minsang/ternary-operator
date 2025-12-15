package com.team3.ternaryoperator.domain.dashboard.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.MyTaskSummaryResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendItemResponse;
import com.team3.ternaryoperator.domain.dashboard.service.DashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // 대시보드 통계
    @GetMapping("/stats")
    public ResponseEntity<CommonResponse<DashboardStatsResponse>> getDashboardStats(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        DashboardStatsResponse response = dashboardService.getDashboardStats(authUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "대시보드 통계 조회 성공."));
    }

    // 주간 작업 추세
    @GetMapping("/weekly-trend")
    public ResponseEntity<CommonResponse<List<WeeklyTrendItemResponse>>> getWeeklyTrend(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<WeeklyTrendItemResponse> response = dashboardService.getWeeklyTrend();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "주간 작업 추세 조회 성공."));
    }

    // 내 작업 요약
    @GetMapping("/tasks")
    public ResponseEntity<CommonResponse<MyTaskSummaryResponse>> getMyTaskSummary(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MyTaskSummaryResponse response = dashboardService.getMyTaskSummary(authUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "내 작업 요약 조회 성공."));
    }
}
