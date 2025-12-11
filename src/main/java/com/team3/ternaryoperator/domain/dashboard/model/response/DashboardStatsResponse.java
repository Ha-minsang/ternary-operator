package com.team3.ternaryoperator.domain.dashboard.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardStatsResponse {
    private final Long totalTasks;
    private final Long completedTasks;
    private final Long inProgressTasks;
    private final Long todoTasks;
    private final Long overdueTasks;
    private final Double teamProgress; // 소수점 2
    private final Double completionRate;

    public static DashboardStatsResponse from(
            Long totalTasks,
            Long completedTasks,
            Long inProgressTasks,
            Long todoTasks,
            Long overdueTasks,
            Double teamProgress,
            Double completionRate
    ) {
        return new DashboardStatsResponse(
                totalTasks,
                completedTasks,
                inProgressTasks,
                todoTasks,
                overdueTasks,
                teamProgress,
                completionRate
        );
    }

}