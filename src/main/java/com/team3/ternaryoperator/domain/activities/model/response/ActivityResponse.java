package com.team3.ternaryoperator.domain.activities.model.response;

import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.domain.user.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ActivityResponse {

    private final Long id;
    private final String type;
    private final Long userId;
    private final UserDto user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

    // 정적 팩토리 메서드
    public static ActivityResponse from(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getType(),
                activity.getActor().getId(),
                UserDto.from(activity.getActor()),
                activity.getTaskId(),
                activity.getTimestamp(),
                activity.getDescription()
        );
    }
}