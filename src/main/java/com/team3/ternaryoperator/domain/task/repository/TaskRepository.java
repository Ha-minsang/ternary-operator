package com.team3.ternaryoperator.domain.task.repository;

import com.team3.ternaryoperator.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
    // 특정 날짜에 생성된 Task 개수
    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE DATE(t.createdAt) = :date")
    long countCreatedByDate(LocalDate date);

    // 특정 날짜에 완료된 Task 개수
    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE DATE(t.updatedAt) = :date " +
            "AND t.status = 'DONE'")
    long countCompletedByDate(LocalDate date);


    List<Task> findAllByAssigneeId(Long id);

    List<Task> findAllByTitleContaining(String query);
}
