package com.team3.ternaryoperator.support;

import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.user.enums.UserRole;

public class UserFixture {
    private static final String DEFAULT_USERNAME = "testUsername";
    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_NAME = "테스트유저";
    private static final String DEFAULT_PASSWORD = "testPassword";
    private static final UserRole DEFAULT_ROLE = UserRole.USER;
    private static final Team DEFAULT_TEAM = null;

    public static User createUser() {
        return new User(DEFAULT_USERNAME, DEFAULT_EMAIL, DEFAULT_NAME, DEFAULT_PASSWORD, DEFAULT_ROLE, DEFAULT_TEAM);
    }
}
