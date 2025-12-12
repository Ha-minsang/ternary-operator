package com.team3.ternaryoperator.domain.activity.model.response;

import com.team3.ternaryoperator.domain.activity.model.dto.ActivityDto;
import com.team3.ternaryoperator.domain.activity.model.dto.ActivityUserResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityResponse {

    private final Long id;
    private final String type;
    private final Long userId;
    private final ActivityUserResponse user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

    public ActivityResponse(Long id, String type, Long userId, ActivityUserResponse user, Long taskId, LocalDateTime timestamp, String description) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.user = user;
        this.taskId = taskId;
        this.timestamp = timestamp;
        this.description = description;
    }


    public static ActivityResponse from(ActivityDto activityDto) {
        return new ActivityResponse(
                activityDto.getId(),
                activityDto.getActivityType().name(),
                activityDto.getUser().getId(),
                ActivityUserResponse.from(activityDto.getUser()),
                activityDto.getTaskId(),
                activityDto.getCreatedAt(),
                activityDto.getDescription()
        );
    }
}
