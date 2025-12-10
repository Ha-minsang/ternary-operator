package com.team3.ternaryoperator.domain.user.repository;

import com.team3.ternaryoperator.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long Id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByTeamIdNot(Long teamId);

    List<User> findByTeamId(Long teamId);

    // User.id와 Team.id를 동시에 만족하는 사용자가 존재하는지 확인
    boolean existsByIdAndTeamId(Long userId, Long teamId);
    
    // 팀 멤버 수 확인
    long countByTeamId(Long id);
}
