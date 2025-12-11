package com.team3.ternaryoperator.domain.task.repository;

import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
    List<Task> findAllByTitleContaining(String query);

    List<Task> findAllByAssigneeId(Long id);

    @Query("""
    SELECT new com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse(
           count(task),
           SUM(CASE WHEN task.status = 'DONE' THEN 1L ELSE 0L END),
           SUM(CASE WHEN task.status = 'IN_PROGRESS' THEN 1L ELSE 0L END),
           SUM(CASE WHEN task.status = 'TODO' THEN 1L ELSE 0L END),
           SUM(CASE WHEN task.status != 'DONE' AND task.dueDate < NOW() THEN 1L ELSE 0L END),
           CAST(ROUND(((SUM(CASE WHEN task.assignee.team.id = user.team.id AND task.status = 'DONE' THEN 1.0 ELSE 0 END) / count(*)) * 100), 2) AS double),
           CAST(ROUND(((SUM(CASE WHEN task.assignee.id = :userId AND task.status = 'DONE' THEN 1.0 ELSE 0 END) / count(*)) * 100), 2) AS double)
    )
    FROM Task task
    LEFT JOIN FETCH User user
      ON task.assignee.id = :userId
    """)
    DashboardStatsResponse findDashboardCounts(@Param("userId") Long userId);
}
