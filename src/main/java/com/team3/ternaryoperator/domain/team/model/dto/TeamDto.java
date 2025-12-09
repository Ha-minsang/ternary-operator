package com.team3.ternaryoperator.domain.team.model.dto;

import com.team3.ternaryoperator.common.entity.Team;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamDto {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;

    public TeamDto(Long id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static TeamDto from(Team team) {
        return new TeamDto(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt()
        );
    }
}
