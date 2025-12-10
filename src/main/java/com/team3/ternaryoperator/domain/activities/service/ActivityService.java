package com.team3.ternaryoperator.domain.activities.service;

import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.domain.activities.model.response.ActivityResponse; // <--- 이 경로가 Controller와 일치하는지 확인
import com.team3.ternaryoperator.domain.activities.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityRepository activityRepository;


    public PageResponse<ActivityResponse> getAllActivities(Pageable pageable) {

        Page<ActivityResponse> page = activityRepository.findAllByOrderByTimestampDesc(pageable)
                .map(ActivityResponse::from);

        return PageResponse.from(page);
    }
}