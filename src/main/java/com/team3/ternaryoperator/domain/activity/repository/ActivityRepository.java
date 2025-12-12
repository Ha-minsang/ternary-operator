package com.team3.ternaryoperator.domain.activity.repository;

import com.team3.ternaryoperator.common.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityRepositoryCustom {

}
