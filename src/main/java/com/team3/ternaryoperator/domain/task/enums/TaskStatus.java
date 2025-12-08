package com.team3.ternaryoperator.domain.task.enums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum TaskStatus {
    TODO("TODO"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private String taskStatus;

    TaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }
}
