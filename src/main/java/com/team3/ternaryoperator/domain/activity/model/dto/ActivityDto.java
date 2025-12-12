package com.team3.ternaryoperator.domain.activity.model.dto;

import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityDto {
    private final Long id;
    private final ActivityType activityType;
    private final User user;
    private final Long taskId;
    private final String description;
    private final LocalDateTime createdAt;

    public ActivityDto(Long id, ActivityType activityType, User user, Long taskId, String description, LocalDateTime createdAt) {
        this.id = id;
        this.activityType = activityType;
        this.user = user;
        this.taskId = taskId;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static ActivityDto from(Activity activity) {
        return new ActivityDto(
                activity.getId(),
                activity.getActivityType(),
                activity.getUser(),
                activity.getTaskId(),
                activity.getDescription(),
                activity.getCreatedAt()
        );
    }

}
