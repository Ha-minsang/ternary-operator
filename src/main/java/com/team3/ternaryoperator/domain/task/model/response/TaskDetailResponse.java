package com.team3.ternaryoperator.domain.task.model.response;

import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.task.model.dto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TaskDetailResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final String priority;
    private final Long assigneeId;
    private final AssigneeResponse assignee;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime dueDate;

    public static TaskDetailResponse from(TaskDto dto, AssigneeResponse assignee) {
        return new TaskDetailResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getStatus(),
                dto.getPriority(),
                dto.getAssignee().getId(),
                assignee,
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getDueDate()
        );
    }






}
