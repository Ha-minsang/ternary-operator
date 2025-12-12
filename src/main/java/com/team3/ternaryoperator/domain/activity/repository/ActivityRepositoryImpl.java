package com.team3.ternaryoperator.domain.activity.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.common.entity.QActivity;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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

        if (cond.getActivityType() != null) {
            builder.and(activity.type.eq(cond.getActivityType().name()));
        }

        if (cond.getTaskId() != null) {
            builder.and(activity.taskId.eq(cond.getTaskId()));
        }

        if (cond.getStartDate() != null) {
            builder.and(activity.timestamp.goe(cond.getStartDate().atStartOfDay()));
        }

        if (cond.getEndDate() != null) {
            builder.and(activity.timestamp.loe(cond.getEndDate().atStartOfDay()));
        }

        // 쿼리 실행
        List<Activity> activities = jpaQueryFactory.selectFrom(activity)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(activity.timestamp.desc()) // 원하는 정렬 기준
                .fetch();

        // 전체 데이터 개수
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
