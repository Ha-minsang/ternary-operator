package com.team3.ternaryoperator.domain.activity.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import com.team3.ternaryoperator.domain.activity.model.dto.ActivityDto;
import com.team3.ternaryoperator.domain.activity.model.response.ActivityResponse;
import com.team3.ternaryoperator.domain.activity.model.response.MyActivityResponse;
import com.team3.ternaryoperator.domain.activity.repository.ActivityRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    @Async
    @Transactional
    public void saveActivity(ActivityType type, Long userId, Long taskId, String taskTitle) {
        String description = type.createDescription(taskTitle);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Activity activity = new Activity(type, user, taskId, description);
        activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivities(ActivitySearchCond condition, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Activity> activities = activityRepository.getActivities(condition, pageable);
        Page<ActivityDto> dtoPage = activities.map(ActivityDto::from);
        return dtoPage.map(ActivityResponse::from);
    }

    public List<MyActivityResponse> getMyActivities(AuthUser authUser) {
        Long userId = authUser.getId();
        List<Activity> activities = activityRepository.findAllActivitiesByUserId(userId);
        List<ActivityDto> dtoList = activities.stream().map(ActivityDto::from).toList();
        return dtoList.stream().map(MyActivityResponse::from).toList();
    }
}
