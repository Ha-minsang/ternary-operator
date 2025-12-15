package com.team3.ternaryoperator.domain.activity.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import com.team3.ternaryoperator.domain.activity.model.response.ActivityResponse;
import com.team3.ternaryoperator.domain.activity.model.response.MyActivityResponse;
import com.team3.ternaryoperator.domain.activity.service.ActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<ActivityResponse>>> getActivities(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute ActivitySearchCond condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<ActivityResponse> response = PageResponse.from(activityService.getActivities(condition, page, size));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "활동 로그 조회 성공."));
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<List<MyActivityResponse>>> getMyActivities(
            @AuthenticationPrincipal com.team3.ternaryoperator.common.dto.AuthUser authUser
    ) {
        List<MyActivityResponse> response = activityService.getMyActivities(authUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "내 활동 로그 조회 성공."));
    }
}
