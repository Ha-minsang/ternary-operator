package com.team3.ternaryoperator.domain.dashboard.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.domain.dashboard.model.dto.DashboardTaskSummaryDto;
import com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.MyTaskSummaryResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendItemResponse;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;

    private final List<String> weekdays = List.of("월", "화", "수", "목", "금", "토", "일");

    // 대시보드 통계 조회
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats(AuthUser authUser) {
        return taskRepository.findDashboardCounts(authUser.getId());
    }

    // 주간 작업 추세 조회
    @Transactional(readOnly = true)
    public List<WeeklyTrendItemResponse> getWeeklyTrend() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<WeeklyTrendItemResponse> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate target = startDate.plusDays(i);
            String weekdayName = weekdays.get(target.getDayOfWeek().getValue() - 1);

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

    // 내 작업 요약 조회
    @Transactional(readOnly = true)
    public MyTaskSummaryResponse getMyTaskSummary(AuthUser authUser) {
        List<Task> tasks = taskRepository.findAllByAssigneeId(authUser.getId());
        return classifyTasks(tasks);
    }

    // 작업 분류
    private MyTaskSummaryResponse classifyTasks(List<Task> tasks) {
        List<DashboardTaskSummaryDto> todayTasks = new ArrayList<>();
        List<DashboardTaskSummaryDto> upcomingTasks = new ArrayList<>();
        List<DashboardTaskSummaryDto> overdueTasks = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if (isSkippableTask(task)) {
                continue;
            }

            DashboardTaskSummaryDto dto = DashboardTaskSummaryDto.from(task);
            addByDueDate(dto, task, today, todayTasks, upcomingTasks, overdueTasks);
        }

        return MyTaskSummaryResponse.of(todayTasks, upcomingTasks, overdueTasks);
    }

    // 제외 대상 Task 여부
    private boolean isSkippableTask(Task task) {
        return task.getDueDate() == null || task.getStatus() == TaskStatus.DONE;
    }

    // 마감일 기준으로 분류
    private void addByDueDate(
            DashboardTaskSummaryDto dto,
            Task task,
            LocalDate today,
            List<DashboardTaskSummaryDto> todayTasks,
            List<DashboardTaskSummaryDto> upcomingTasks,
            List<DashboardTaskSummaryDto> overdueTasks
    ) {
        LocalDate dueDate = task.getDueDate().toLocalDate();

        if (dueDate.isEqual(today)) {
            todayTasks.add(dto);
            return;
        }

        if (dueDate.isAfter(today)) {
            upcomingTasks.add(dto);
            return;
        }

        overdueTasks.add(dto);
    }
}
