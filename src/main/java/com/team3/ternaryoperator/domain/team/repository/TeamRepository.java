package com.team3.ternaryoperator.domain.team.repository;

import com.team3.ternaryoperator.common.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // 팀 이름 중복 체크
    boolean existsByName(String name);

    // deletedAt 기반 소프트 딜리트
    List<Team> findAllByDeletedAtIsNull();

    List<Team> findAllByNameContaining(String query);
}