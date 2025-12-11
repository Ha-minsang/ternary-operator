package com.team3.ternaryoperator.domain.activity.service;

import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import com.team3.ternaryoperator.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Async
    @Transactional
    public void saveActivity(ActivityType type, Long userId, Long taskId) {
        Activity activity = new Activity(type, userId, taskId);
        activityRepository.save(activity);
    }
}
