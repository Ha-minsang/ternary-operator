package com.team3.ternaryoperator.domain.task.repository;

import com.team3.ternaryoperator.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    List<Task> findAllByAssigneeId(Long id);
}
