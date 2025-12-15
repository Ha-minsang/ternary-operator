package com.team3.ternaryoperator.common.dto;

import com.team3.ternaryoperator.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {
    private final Long id;
    private final String username;
    private final UserRole role;
}
