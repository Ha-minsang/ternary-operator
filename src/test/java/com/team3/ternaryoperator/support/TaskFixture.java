package com.team3.ternaryoperator.support;

import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.task.enums.TaskPriority;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;

import java.time.LocalDateTime;

public class TaskFixture {

    private static final String DEFAULT_TITLE = "test title";
    private static final String DEFAULT_DESCRIPTION = "test description";
    private static final TaskStatus DEFAULT_STATUS = TaskStatus.TODO;
    private static final TaskPriority DEFAULT_PRIORITY = TaskPriority.MEDIUM;
    private static final LocalDateTime DEFAULT_DUE_DATE = LocalDateTime.now();

    public static Task createTask() {
        User assignee = UserFixture.createUser();

        return new Task(
                DEFAULT_TITLE,
                DEFAULT_DESCRIPTION,
                DEFAULT_STATUS,
                DEFAULT_PRIORITY,
                assignee,
                DEFAULT_DUE_DATE
        );
    }
}
