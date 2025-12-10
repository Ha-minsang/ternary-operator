package com.team3.ternaryoperator.domain.dashboard.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.MyTaskSummaryResponse;
import com.team3.ternaryoperator.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

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
