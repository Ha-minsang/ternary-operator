package com.team3.ternaryoperator.domain.team.model.dto;

import com.team3.ternaryoperator.common.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamMemberDetailDto {

    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final String role;
    private final LocalDateTime createdAt;

    public TeamMemberDetailDto(Long id, String username, String name, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static TeamMemberDetailDto from(User user) {
        return new TeamMemberDetailDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}