package com.team3.ternaryoperator.domain.search.service;

import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.common.exception.SearchException;
import com.team3.ternaryoperator.domain.search.model.TaskSummaryDto;
import com.team3.ternaryoperator.domain.search.model.TeamSummaryDto;
import com.team3.ternaryoperator.domain.search.model.UserSummaryDto;
import com.team3.ternaryoperator.domain.search.model.response.SearchResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // 검색
    public SearchResponse search(String query) {
        validateQuery(query);

        List<TaskSummaryDto> taskDtoList = taskRepository.findAllByTitleContaining(query).stream()
                .map(TaskSummaryDto::from)
                .toList();

        List<TeamSummaryDto> teamDtoList = teamRepository.findAllByNameContaining(query).stream()
                .map(TeamSummaryDto::from)
                .toList();

        List<UserSummaryDto> userDtoList = userRepository.findAllByNameContaining(query).stream()
                .map(UserSummaryDto::from)
                .toList();

        return new SearchResponse(taskDtoList, teamDtoList, userDtoList);
    }

    // 검색어 검증 (없으면 예외 발생)
    private void validateQuery(String query) {
        if (query == null || query.isEmpty()) {
            throw new SearchException(ErrorCode.SEARCH_QUERY_EMPTY);
        }
    }
}
