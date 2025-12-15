package com.team3.ternaryoperator.domain.search.service;

import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.search.model.response.SearchResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import com.team3.ternaryoperator.support.TaskFixture;
import com.team3.ternaryoperator.support.TeamFixture;
import com.team3.ternaryoperator.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    @DisplayName("검색 성공 - task/team/user 결과 반환")
    void search_ShouldReturnSearchResponse() {
        // given
        String query = "test";

        Task task = TaskFixture.createTask();
        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser();

        given(taskRepository.findAllByTitleContaining(query)).willReturn(List.of(task));
        given(teamRepository.findAllByNameContaining(query)).willReturn(List.of(team));
        given(userRepository.findAllByNameContaining(query)).willReturn(List.of(user));

        // when
        SearchResponse response = searchService.search(query);

        // then
        assertNotNull(response);

        List<?> tasks = response.getTasks();
        List<?> teams = response.getTeams();
        List<?> users = response.getUsers();

        assertEquals(1, tasks.size());
        assertEquals(1, teams.size());
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("검색 실패 - query가 null")
    void search_ShouldThrowException_WhenQueryIsNull() {
        // given
        String query = null;

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> searchService.search(query)
        );

        // then
        assertEquals(ErrorCode.SEARCH_QUERY_EMPTY, exception.getErrorCode());
    }
}
