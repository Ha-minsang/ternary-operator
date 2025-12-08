package com.team3.ternaryoperator.domain.task.model.dto;

import com.team3.ternaryoperator.common.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    public static TaskDto from(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getTaskStatus(),
                task.getPriority().getTaskPriority(),
                task.getAssignee().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate()
        );
    }
}