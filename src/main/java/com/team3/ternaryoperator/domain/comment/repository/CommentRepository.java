package com.team3.ternaryoperator.domain.comment.repository;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);

    List<Comment> findByParentComment_Id(Long parentId);

    // 부모 댓글 페이징 조회
    @Query("""
    SELECT c FROM Comment c
    WHERE c.task = :task
      AND c.parentComment IS NULL
""")
    Page<Comment> findParentCommentsByTask(Task task, Pageable pageable);

    // 특정 부모 댓글들 자식 댓글 조회
    @Query("""
    SELECT c FROM Comment c
    WHERE c.parentComment.id IN :parentIds
    ORDER BY c.createdAt DESC
""")
    List<Comment> findRepliesByParentIds(List<Long> parentIds);

}
