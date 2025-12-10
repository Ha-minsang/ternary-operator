package com.team3.ternaryoperator.domain.comment.repository;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTask(Task task, Pageable pageable);

    Optional<Comment> findById(Long id);

    List<Comment> findByParentComment_Id(Long parentId);
}
