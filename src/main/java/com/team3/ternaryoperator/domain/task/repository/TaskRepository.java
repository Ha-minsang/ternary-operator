package com.team3.ternaryoperator.domain.task.repository;

import com.team3.ternaryoperator.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
