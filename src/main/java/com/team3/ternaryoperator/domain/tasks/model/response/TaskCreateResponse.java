package com.team3.ternaryoperator.domain.tasks.model.response;

import com.team3.ternaryoperator.domain.tasks.model.dto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TaskCreateResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final String priority;
    private final Long assigneeId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime dueDate;

    public static TaskCreateResponse from(TaskDto dto) {
        return new TaskCreateResponse(
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