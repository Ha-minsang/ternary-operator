package com.team3.ternaryoperator.domain.search.model;

import com.team3.ternaryoperator.common.entity.Task;
import lombok.Getter;

@Getter
public class TaskSummaryDto {

    private final Long id;
    private final String title;
    private final String description;
    private final String status;

    public TaskSummaryDto(Long id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public static TaskSummaryDto from(Task task) {
        return new TaskSummaryDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getTaskStatus()
        );
    }
}
