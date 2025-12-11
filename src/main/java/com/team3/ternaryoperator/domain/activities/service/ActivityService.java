package com.team3.ternaryoperator.domain.activities.service;

import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.activities.model.response.ActivityResponse;
import com.team3.ternaryoperator.domain.activities.repository.ActivityRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public PageResponse<ActivityResponse> getAllActivities(Pageable pageable) {

        Page<Activity> activityPage = activityRepository.findAllByOrderByTimestampDesc(pageable);


        return PageResponse.from(activityPage.map(ActivityResponse::from));
    }
}