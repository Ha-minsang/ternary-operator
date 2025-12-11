package com.team3.ternaryoperator.domain.activities.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.domain.activities.model.response.ActivityResponse;
import com.team3.ternaryoperator.domain.activities.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/activities")
    public CommonResponse<PageResponse<ActivityResponse>> getActivities(@PageableDefault(
            size = 10,
            page = 0,
            sort = "timestamp"
    )Pageable pageable) {
        PageResponse<ActivityResponse> result = activityService.getAllActivities(pageable);
        return CommonResponse.success(result, "활동 로그 조회 성공");
    }


}