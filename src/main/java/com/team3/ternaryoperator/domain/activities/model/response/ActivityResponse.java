package com.team3.ternaryoperator.domain.activities.model.response;

import com.team3.ternaryoperator.common.entity.Activity;

import com.team3.ternaryoperator.domain.user.dto.CustomUserDto;
import com.team3.ternaryoperator.domain.user.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ActivityResponse {

    private final Long id;
    private final String type;
    private final CustomUserDto user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;


    // 정적 메서드
    public static ActivityResponse from(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getType(),
                CustomUserDto.from(activity.getUser()),
                activity.getTaskId(),
                activity.getTimestamp(),
                activity.getDescription()
        );
    }
}