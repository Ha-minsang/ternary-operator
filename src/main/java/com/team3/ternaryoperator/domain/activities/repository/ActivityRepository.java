package com.team3.ternaryoperator.domain.activities.repository;

import com.team3.ternaryoperator.common.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByOrderByTimestampDesc(Pageable pageable);
}