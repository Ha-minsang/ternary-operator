package com.team3.ternaryoperator.domain.activity.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.common.entity.QActivity;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ActivityRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Activity> getActivities(ActivitySearchCond cond, Pageable pageable) {
        QActivity activity = QActivity.activity;
        BooleanBuilder builder = new BooleanBuilder();

        if (cond.getType() != null) {
            builder.and(activity.activityType.eq(ActivityType.valueOf(cond.getType())));
        }

        if (cond.getTaskId() != null) {
            builder.and(activity.taskId.eq(cond.getTaskId()));
        }

        if (cond.getStartDate() != null) {
            LocalDateTime start = cond.getStartDate().atStartOfDay();
            builder.and(activity.createdAt.goe(start));
        }

        if (cond.getEndDate() != null) {
            LocalDateTime endExclusive = cond.getEndDate().plusDays(1).atStartOfDay();
            builder.and(activity.createdAt.lt(endExclusive));
        }

        List<Activity> activities = jpaQueryFactory.selectFrom(activity)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(activity.createdAt.desc()) // 원하는 정렬 기준
                .fetch();

        Long total = jpaQueryFactory.select(activity.count())
                .from(activity)
                .where(builder)
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(activities, pageable, total);
    }
}
