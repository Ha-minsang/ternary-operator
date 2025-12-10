package com.team3.ternaryoperator.domain.activities.service;

import com.team3.ternaryoperator.common.aop.TrackTime;
import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.domain.activities.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityLogger {

    private final ActivityRepository activityRepository;


    @TrackTime
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveActivityLog(Activity activity) {
        activityRepository.save(activity);
    }
}