package com.team3.ternaryoperator.domain.activity.model.dto;

import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import lombok.Getter;

@Getter
public class ActivityDto {
    private Long id;
    private ActivityType activityType;
    private Long userId;
    private Long taskId;

    public ActivityDto(Long id, ActivityType activityType, Long userId, Long taskId) {
        this.id = id;
        this.activityType = activityType;
        this.userId = userId;
        this.taskId = taskId;
    }

    public static ActivityDto from(Activity activity) {
        return new ActivityDto(
                activity.getId(),
                activity.getActivityType(),
                activity.getUserId(),
                activity.getTaskId());
    }

}
