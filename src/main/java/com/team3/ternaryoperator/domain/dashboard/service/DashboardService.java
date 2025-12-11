package com.team3.ternaryoperator.domain.dashboard.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.domain.dashboard.model.dto.DashboardTaskSummaryDto;
import com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.MyTaskSummaryResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendItemResponse;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;

    private static final List<String> WEEKDAYS = List.of("월", "화", "수", "목", "금", "토", "일");

    // 대시보드 통계
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats(AuthUser authUser) {
        return taskRepository.findDashboardCounts(authUser.getId());
    }

    // 주간 작업 추세
    public List<WeeklyTrendItemResponse> getWeeklyTrend() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<WeeklyTrendItemResponse> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            LocalDate target = startDate.plusDays(i);

            String weekdayName = WEEKDAYS.get(target.getDayOfWeek().getValue() - 1);

            long created = taskRepository.countCreatedByDate(target);
            long completed = taskRepository.countCompletedByDate(target);

            result.add(new WeeklyTrendItemResponse(
                    weekdayName,
                    created,
                    completed,
                    target.toString()
            ));
        }
        return result;
    }

    // 내 작업 요약
    public MyTaskSummaryResponse getMyTaskSummary(AuthUser authUser) {
        List<Task> tasks = taskRepository.findAllByAssigneeId(authUser.getId());

        List<DashboardTaskSummaryDto> todayTasks = new ArrayList<>();
        List<DashboardTaskSummaryDto> upcomingTasks = new ArrayList<>();
        List<DashboardTaskSummaryDto> overdueTasks = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (Task task : tasks) {
            if (task.getDueDate() == null) {
                continue;
            }
            if (task.getStatus() == TaskStatus.DONE) {
                continue;
            }

            LocalDate dueDate = task.getDueDate().toLocalDate();
            DashboardTaskSummaryDto dto = DashboardTaskSummaryDto.from(task);

            if (dueDate.isEqual(today)) {
                todayTasks.add(dto);
            } else if (dueDate.isAfter(today)) {
                upcomingTasks.add(dto);
            } else {
                overdueTasks.add(dto);
            }
        }
        return MyTaskSummaryResponse.of(todayTasks, upcomingTasks, overdueTasks);
    }
}