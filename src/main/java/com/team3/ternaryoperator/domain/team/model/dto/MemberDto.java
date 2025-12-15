package com.team3.ternaryoperator.domain.team.model.dto;

import com.team3.ternaryoperator.common.entity.User;
import lombok.Getter;

@Getter
public class MemberDto {

    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final String role;

    public MemberDto(Long id, String username, String name, String email, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static MemberDto from(User user) {
        return new MemberDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
