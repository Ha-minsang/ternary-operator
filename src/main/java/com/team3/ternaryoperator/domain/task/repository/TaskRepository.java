package com.team3.ternaryoperator.domain.task.repository;

import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.team3.ternaryoperator.common.exception.ErrorCode.TASK_NOT_FOUND;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

}
