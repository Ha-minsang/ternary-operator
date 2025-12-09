package com.team3.ternaryoperator.domain.task.model.response;

import com.team3.ternaryoperator.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssigneeResponse {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;

    public static AssigneeResponse from(User user) {
        return new AssigneeResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        );
    }
}