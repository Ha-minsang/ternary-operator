package com.team3.ternaryoperator.domain.dashboard.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.MyTaskSummaryResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendItemResponse;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.support.AuthUserFixture;
import com.team3.ternaryoperator.support.TaskFixture;
import com.team3.ternaryoperator.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    @DisplayName("대시보드 통계 조회 성공")
    void getDashboardStats_ShouldReturnStats() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        DashboardStatsResponse expected = org.mockito.Mockito.mock(DashboardStatsResponse.class);

        given(taskRepository.findDashboardCounts(authUser.getId())).willReturn(expected);

        // when
        DashboardStatsResponse response = dashboardService.getDashboardStats(authUser);

        // then
        assertNotNull(response);
        assertSame(expected, response);
    }

    @Test
    @DisplayName("주간 작업 추세 조회 성공 - 7일치 반환")
    void getWeeklyTrend_ShouldReturnSevenItems() {
        // given
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        for (int i = 0; i < 7; i++) {
            LocalDate target = startDate.plusDays(i);
            given(taskRepository.countCreatedByDate(target)).willReturn((long) (i + 1));
            given(taskRepository.countCompletedByDate(target)).willReturn((long) (i + 10));
        }

        // when
        List<WeeklyTrendItemResponse> result = dashboardService.getWeeklyTrend();

        // then
        assertNotNull(result);
        assertEquals(7, result.size());
    }

    @Test
    @DisplayName("내 작업 요약 성공 - 오늘/예정/지남 분류")
    void getMyTaskSummary_ShouldClassifyTasks() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        LocalDate today = LocalDate.now();

        Task todayTask = TaskFixture.createTask(UserFixture.createUser());
        ReflectionTestUtils.setField(todayTask, "dueDate", today.atTime(10, 0));
        ReflectionTestUtils.setField(todayTask, "status", TaskStatus.TODO);

        Task todoTask = TaskFixture.createTask(UserFixture.createUser());
        ReflectionTestUtils.setField(todoTask, "dueDate", today.plusDays(1).atTime(10, 0));
        ReflectionTestUtils.setField(todoTask, "status", TaskStatus.IN_PROGRESS);

        Task overdueTask = TaskFixture.createTask(UserFixture.createUser());
        ReflectionTestUtils.setField(overdueTask, "dueDate", today.minusDays(1).atTime(10, 0));
        ReflectionTestUtils.setField(overdueTask, "status", TaskStatus.TODO);

        Task doneTask = TaskFixture.createTask(UserFixture.createUser());
        ReflectionTestUtils.setField(doneTask, "dueDate", today.atTime(10, 0));
        ReflectionTestUtils.setField(doneTask, "status", TaskStatus.DONE);

        Task nullDueDateTask = TaskFixture.createTask(UserFixture.createUser());
        ReflectionTestUtils.setField(nullDueDateTask, "dueDate", (LocalDateTime) null);
        ReflectionTestUtils.setField(nullDueDateTask, "status", TaskStatus.TODO);

        given(taskRepository.findAllByAssigneeId(authUser.getId()))
                .willReturn(List.of(todayTask, todoTask, overdueTask, doneTask, nullDueDateTask));

        // when
        MyTaskSummaryResponse response = dashboardService.getMyTaskSummary(authUser);

        // then
        assertNotNull(response);
        assertEquals(1, response.getTodayTasks().size());
        assertEquals(1, response.getTodoTasks().size());
        assertEquals(1, response.getOverdueTasks().size());
    }

}
