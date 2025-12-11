package com.team3.ternaryoperator.domain.team.model.response;

import com.team3.ternaryoperator.domain.team.model.dto.TeamMemberDetailDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamGetMemberResponse {

    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final String role;
    private final LocalDateTime createdAt;

    public TeamGetMemberResponse(Long id, String username, String name, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static TeamGetMemberResponse from(TeamMemberDetailDto dto) {
        return new TeamGetMemberResponse(
                dto.getId(),
                dto.getUsername(),
                dto.getName(),
                dto.getEmail(),
                dto.getRole(),
                dto.getCreatedAt()
        );
    }
}