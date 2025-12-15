package com.team3.ternaryoperator.support;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.domain.user.enums.UserRole;

public class AuthUserFixture {
    private static final long DEFAULT_ID = 1L;
    private static final String DEFAULT_USERNAME = "testUserName";
    private static final UserRole DEFAULT_ROLE = UserRole.USER;

    public static AuthUser createAuthUser() {
        return new AuthUser(DEFAULT_ID, DEFAULT_USERNAME, DEFAULT_ROLE);
    }
}
