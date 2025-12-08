package com.team3.ternaryoperator.common.dto;

import com.team3.ternaryoperator.domain.users.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {
    private final Long id;
    private final String email;
    private final UserRole role;
}
