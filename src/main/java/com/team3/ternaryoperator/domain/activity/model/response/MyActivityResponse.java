package com.team3.ternaryoperator.domain.activity.model.response;

import com.team3.ternaryoperator.domain.activity.model.dto.ActivityDto;
import com.team3.ternaryoperator.domain.activity.model.dto.ActivityUserResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyActivityResponse {

    private final Long id;
    private final Long userId;
    private final ActivityUserResponse user;
    private final String action;
    private final String targetType;
    private final Long targetId;
    private final String description;
    private final LocalDateTime createdAt;

    public MyActivityResponse(Long id, Long userId, ActivityUserResponse user, String action, String targetType, Long targetId, String description, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.description = description;
        this.createdAt = createdAt;
    }


    public static MyActivityResponse from(ActivityDto activityDto) {
        return new MyActivityResponse(
                activityDto.getId(),
                activityDto.getUser().getId(),
                ActivityUserResponse.from(activityDto.getUser()),
                activityDto.getActivityType().getAction(),
                activityDto.getActivityType().name(),
                activityDto.getTaskId(),
                activityDto.getDescription(),
                activityDto.getCreatedAt()
        );
    }
}
