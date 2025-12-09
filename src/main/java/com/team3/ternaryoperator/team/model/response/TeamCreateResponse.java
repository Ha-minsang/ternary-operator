package com.team3.ternaryoperator.team.model.response;

import com.team3.ternaryoperator.common.entity.Team;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamCreateResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;

    public TeamCreateResponse(Long id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static TeamCreateResponse from(Team team) {
        return new TeamCreateResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt()
        );
    }
}