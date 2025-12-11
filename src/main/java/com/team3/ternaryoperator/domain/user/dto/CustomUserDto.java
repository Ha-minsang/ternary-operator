package com.team3.ternaryoperator.domain.user.dto;

import com.team3.ternaryoperator.common.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDto {

    private final Long id;
    private final String username;
    private final String name;

    public static CustomUserDto from(User user) {
        return new CustomUserDto(
                user.getId(),
                user.getUsername(),
                user.getName()
        );
    }
}


