package com.team3.ternaryoperator.domain.user.model.response;

import com.team3.ternaryoperator.domain.user.enums.UserRole;
import com.team3.ternaryoperator.domain.user.model.dto.UserDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDetailResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private UserDetailResponse(Long id, String username, String email, String name, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserDetailResponse from(UserDto userDto) {
        return new UserDetailResponse(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getName(),
                userDto.getRole(),
                userDto.getCreatedAt(),
                userDto.getUpdatedAt()
        );
    }
}
