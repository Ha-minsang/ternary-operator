package com.team3.ternaryoperator.domain.task.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.ternaryoperator.common.entity.QTask;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public TaskRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Task> getTasks(String status, String search, Long assigneeId, Pageable pageable) {
        QTask task = QTask.task;

        BooleanBuilder builder = new BooleanBuilder();

        if(status != null && !status.isBlank()) {
            TaskStatus taskStatus = TaskStatus.valueOf(status);
            builder.and(task.status.eq(taskStatus));
        }

        if(search != null && !search.isBlank()) {
            builder.and(task.title.containsIgnoreCase(search).or(task.description.containsIgnoreCase(search)));
        }

        if(assigneeId != null) {
            builder.and(task.assignee.id.eq(assigneeId));
        }

        List<Task> tasks = jpaQueryFactory.selectFrom(task)
                                            .where(builder)
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .orderBy(task.createdAt.desc())
                                            .fetch();

        Long total = jpaQueryFactory.select(task.count())
                                    .from(task)
                                    .where(builder)
                                    .fetchOne();

        if(total == null) {
            total = 0L;
        }

        return new PageImpl<>(tasks, pageable, total);
    }
}
