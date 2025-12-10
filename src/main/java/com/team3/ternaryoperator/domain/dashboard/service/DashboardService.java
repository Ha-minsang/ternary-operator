package com.team3.ternaryoperator.domain.dashboard.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.domain.dashboard.model.dto.DashboardTaskSummaryDto;
import com.team3.ternaryoperator.domain.dashboard.model.response.MyTaskSummaryResponse;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;

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
