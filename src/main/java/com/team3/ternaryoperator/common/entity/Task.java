package com.team3.ternaryoperator.common.entity;

import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.domain.task.enums.TaskPriority;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.model.request.TaskUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

import static com.team3.ternaryoperator.common.exception.ErrorCode.TASK_INVALID_STATUS;

@Getter
@Entity
@Table(name = "tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignee_id", nullable = false)
    private User assignee;

    @Column
    private LocalDateTime dueDate;

    public Task(String title, String description, TaskStatus status, TaskPriority priority, User assignee, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.dueDate = dueDate;
    }

    // 업데이트
    public void update(TaskUpdateRequest request, User newAssignee) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        if (request.getStatus() != null) {
            this.status = TaskStatus.valueOf(request.getStatus());
        }
        this.priority = TaskPriority.valueOf(request.getPriority());
        this.assignee = newAssignee;
        this.dueDate = request.getDueDate();
    }

    // 상태 변경
    public void changeStatus(TaskStatus newStatus) {
        if (this.status == TaskStatus.TODO && newStatus == TaskStatus.IN_PROGRESS) {
            this.status = newStatus;
        } else if (this.status == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.DONE) {
            this.status = newStatus;
        } else {
            throw new CustomException(TASK_INVALID_STATUS);
        }
    }
}