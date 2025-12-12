package com.team3.ternaryoperator.domain.activity.service;

import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import com.team3.ternaryoperator.domain.activity.model.dto.ActivityDto;
import com.team3.ternaryoperator.domain.activity.model.response.ActivityResponse;
import com.team3.ternaryoperator.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Async
    @Transactional
    public void saveActivity(ActivityType type, Long userId, Long taskId, String taskTitle) {
        String description = type.createDescription(taskTitle);
        Activity activity = new Activity(type, userId, taskId, description);
        activityRepository.save(activity);
    }

    public Page<ActivityResponse> getActivities(ActivitySearchCond condition, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Activity> activities = activityRepository.getActivities(condition, pageable);
        Page<ActivityDto> dtoPage = activities.map(ActivityDto::from);
        
        return dtoPage.map(ActivityResponse::from);
    }
}
