package com.team3.ternaryoperator.team.repository;

import com.team3.ternaryoperator.common.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // 팀 이름 중복 체크
    boolean existsByName(String name);
}