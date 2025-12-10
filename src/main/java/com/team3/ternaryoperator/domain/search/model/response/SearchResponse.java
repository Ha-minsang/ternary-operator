package com.team3.ternaryoperator.domain.search.model.response;

import com.team3.ternaryoperator.domain.search.model.TaskSummaryDto;
import com.team3.ternaryoperator.domain.search.model.TeamSummaryDto;
import com.team3.ternaryoperator.domain.search.model.UserSummaryDto;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchResponse {

    private final List<TaskSummaryDto> tasks;
    private final List<TeamSummaryDto> teams;
    private final List<UserSummaryDto> users;

    public SearchResponse(List<TaskSummaryDto> tasks, List<TeamSummaryDto> teams, List<UserSummaryDto> users) {
        this.tasks = tasks;
        this.teams = teams;
        this.users = users;
    }

    public static SearchResponse of(
            List<TaskSummaryDto> tasks,
            List<TeamSummaryDto> teams,
            List<UserSummaryDto> users
    ) {
        return new SearchResponse(tasks, teams, users);
    }
}
