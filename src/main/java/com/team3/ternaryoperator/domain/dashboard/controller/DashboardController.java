package com.team3.ternaryoperator.domain.dashboard.controller;


import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse;
import com.team3.ternaryoperator.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/stats")
    public ResponseEntity<CommonResponse<DashboardStatsResponse>> getDashboardStats(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        DashboardStatsResponse response = dashboardService.getDashboardStats(authUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "대시보드 통계 조회 성공"));
    }




}
