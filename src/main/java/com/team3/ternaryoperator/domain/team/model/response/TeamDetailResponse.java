package com.team3.ternaryoperator.domain.team.model.response;

import com.team3.ternaryoperator.domain.team.model.dto.MemberDto;
import com.team3.ternaryoperator.domain.team.model.dto.TeamDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TeamDetailResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<MemberDto> members;

    public TeamDetailResponse(
            Long id,
            String name,
            String description,
            LocalDateTime createdAt,
            List<MemberDto> members) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.members = members;
    }

    public static TeamDetailResponse fromDetail(TeamDto dto, List<MemberDto> members) {
        return new TeamDetailResponse(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getCreatedAt(),
                members
        );
    }
}