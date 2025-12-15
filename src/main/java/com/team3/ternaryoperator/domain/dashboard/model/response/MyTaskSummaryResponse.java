package com.team3.ternaryoperator.domain.dashboard.model.response;

import com.team3.ternaryoperator.domain.dashboard.model.dto.DashboardTaskSummaryDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MyTaskSummaryResponse {

    private final List<DashboardTaskSummaryDto> todayTasks;
    private final List<DashboardTaskSummaryDto> todoTasks;
    private final List<DashboardTaskSummaryDto> overdueTasks;

    public MyTaskSummaryResponse(List<DashboardTaskSummaryDto> todayTasks, List<DashboardTaskSummaryDto> todoTasks, List<DashboardTaskSummaryDto> overdueTasks) {
        this.todayTasks = todayTasks;
        this.todoTasks = todoTasks;
        this.overdueTasks = overdueTasks;
    }

    public static MyTaskSummaryResponse of(
            List<DashboardTaskSummaryDto> tasks,
            List<DashboardTaskSummaryDto> todoTasks,
            List<DashboardTaskSummaryDto> overdueTasks
    ) {
        return new MyTaskSummaryResponse(tasks, todoTasks, overdueTasks);
    }
}
