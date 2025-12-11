package com.team3.ternaryoperator.domain.activity.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@AllArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Activity> getActivities(ActivitySearchCond cond, Pageable pageable) {
    }
