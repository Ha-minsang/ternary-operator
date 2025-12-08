package com.team3.ternaryoperator.domain.users.repository;

import com.team3.ternaryoperator.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}