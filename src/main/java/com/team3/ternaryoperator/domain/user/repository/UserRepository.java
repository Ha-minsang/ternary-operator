package com.team3.ternaryoperator.domain.user.repository;

import com.team3.ternaryoperator.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long Id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
