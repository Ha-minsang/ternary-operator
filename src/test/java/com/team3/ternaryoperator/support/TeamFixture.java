package com.team3.ternaryoperator.support;

import com.team3.ternaryoperator.common.entity.Team;

public class TeamFixture {

    private static final String DEFAULT_NAME = "testTeam";
    private static final String DEFAULT_DESCRIPTION = "test team description";

    public static Team createTeam() {
        return new Team(
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION
        );
    }
}
