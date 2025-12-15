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
import java.util.List;
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
    private final UserRepository userRepository;

    // Activity 저장
    @Async
    @Transactional
    public void saveActivity(ActivityType type, Long userId, Long taskId, String taskTitle) {
        User user = getUserOrThrow(userId);
        String description = type.createDescription(taskTitle);
        activityRepository.save(new Activity(type, user, taskId, description));
    }

    // Activity 조회
    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivities(ActivitySearchCond condition, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return activityRepository.getActivities(condition, pageable)
                .map(ActivityDto::from)
                .map(ActivityResponse::from);
    }

    // 내 Activity 조회
    @Transactional(readOnly = true)
    public List<MyActivityResponse> getMyActivities(AuthUser authUser) {
        return activityRepository.findAllActivitiesByUserId(authUser.getId()).stream()
                .map(ActivityDto::from)
                .map(MyActivityResponse::from)
                .toList();
    }

    // User 찾기 (없으면 예외 발생)
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
