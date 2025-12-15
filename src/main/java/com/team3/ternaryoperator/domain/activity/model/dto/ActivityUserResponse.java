package com.team3.ternaryoperator.domain.activity.model.dto;

import com.team3.ternaryoperator.common.entity.User;
import lombok.Getter;

@Getter
public class ActivityUserResponse {
    private final Long id;
    private final String name;
    private final String username;

    public ActivityUserResponse(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public static ActivityUserResponse from(User user) {
        return new ActivityUserResponse(user.getId(), user.getUsername(), user.getName());
    }
}

