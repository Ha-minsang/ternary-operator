package com.team3.ternaryoperator.domain.users.dto.response;

import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.users.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCreateResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final UserRole role;
    private final LocalDateTime createdAt;

    public UserCreateResponse(Long id, String username, String email, String name, UserRole role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserCreateResponse from(User user) {
        return new UserCreateResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
