package com.team3.ternaryoperator.domain.search.model;

import com.team3.ternaryoperator.common.entity.Team;
import lombok.Getter;

@Getter
public class TeamSummaryDto {

    private final Long id;
    private final String name;
    private final String description;

    public TeamSummaryDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static TeamSummaryDto from(Team team) {
        return new TeamSummaryDto(team.getId(), team.getName(), team.getDescription());
    }
}
