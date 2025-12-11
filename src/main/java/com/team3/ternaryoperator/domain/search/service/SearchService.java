package com.team3.ternaryoperator.domain.search.service;

import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.search.model.TaskSummaryDto;
import com.team3.ternaryoperator.domain.search.model.TeamSummaryDto;
import com.team3.ternaryoperator.domain.search.model.UserSummaryDto;
import com.team3.ternaryoperator.domain.search.model.response.SearchResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public SearchResponse search(String query) {
        if (query == null || query.isEmpty()) {
            throw new CustomException(ErrorCode.SEARCH_QUERY_EMPTY);
        }
        List<Task> tasks = taskRepository.findAllByTitleContaining(query);
        List<Team> teams = teamRepository.findAllByNameContaining(query);
        List<User> users = userRepository.findAllByNameContaining(query);

        List<TaskSummaryDto> taskDtoList = tasks.stream()
                .map(TaskSummaryDto::from)
                .toList();

        List<TeamSummaryDto> teamDtoList = teams.stream()
                .map(TeamSummaryDto::from)
                .toList();

        List<UserSummaryDto> userDtoList = users.stream()
                .map(UserSummaryDto::from)
                .toList();

        return new SearchResponse(taskDtoList, teamDtoList, userDtoList);
    }
}
