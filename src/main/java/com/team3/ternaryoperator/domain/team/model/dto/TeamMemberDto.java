package com.team3.ternaryoperator.domain.team.model.dto;

import com.team3.ternaryoperator.common.entity.User;
import lombok.Getter;

@Getter
public class TeamMemberDto {

    private final Long id;
    private final String username;
    private final String name;

    public TeamMemberDto(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public static TeamMemberDto from(User user) {
        return new TeamMemberDto(
                user.getId(),
                user.getUsername(),
                user.getName()
        );
    }
}