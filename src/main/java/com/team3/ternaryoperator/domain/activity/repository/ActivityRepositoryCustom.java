package com.team3.ternaryoperator.domain.activity.repository;

import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.domain.activity.model.condition.ActivitySearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityRepositoryCustom {

    Page<Activity> getActivities(ActivitySearchCond cond, Pageable pageable);
}
