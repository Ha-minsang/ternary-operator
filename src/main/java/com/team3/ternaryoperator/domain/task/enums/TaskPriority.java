package com.team3.ternaryoperator.domain.task.enums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum TaskPriority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private String taskPriority;

    TaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskPriority() {
        return taskPriority;
    }
}
