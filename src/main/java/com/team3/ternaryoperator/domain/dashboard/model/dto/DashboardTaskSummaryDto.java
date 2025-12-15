package com.team3.ternaryoperator.domain.dashboard.model.dto;

import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.domain.user.model.dto.UserDto;
import com.team3.ternaryoperator.domain.user.model.response.UserResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DashboardTaskSummaryDto {

    private final Long id;
    private final UserResponse assignee;
    private final String title;
    private final String status;
    private final String priority;
    private final LocalDateTime dueDate;

    public DashboardTaskSummaryDto(Long id, UserResponse assignee, String title, String status, String priority, LocalDateTime dueDate) {
        this.id = id;
        this.assignee = assignee;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public static DashboardTaskSummaryDto from(Task task) {
        return new DashboardTaskSummaryDto(
                task.getId(),
                UserResponse.from(UserDto.from(task.getAssignee())),
                task.getTitle(),
                task.getStatus().getTaskStatus(),
                task.getPriority().getTaskPriority(),
                task.getDueDate());
    }
}
