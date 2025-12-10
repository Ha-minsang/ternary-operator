package com.team3.ternaryoperator.domain.task.model.response;

import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.task.model.dto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TaskResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final String priority;
    private final User assignee;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime dueDate;

    public static TaskResponse from(TaskDto dto) {
        return new TaskResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getStatus(),
                dto.getPriority(),
                dto.getAssignee(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getDueDate()
        );
    }
}