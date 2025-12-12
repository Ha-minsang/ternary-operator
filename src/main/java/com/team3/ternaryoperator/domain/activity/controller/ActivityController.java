package com.team3.ternaryoperator.domain.activity.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import com.team3.ternaryoperator.domain.activity.model.response.ActivityResponse;
import com.team3.ternaryoperator.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<ActivityResponse>>> getActivities(
            @ModelAttribute ActivitySearchCond condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<ActivityResponse> response = PageResponse.from(activityService.getActivities(condition, page, size));
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<response>);
    }

}
