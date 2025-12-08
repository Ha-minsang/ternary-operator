package com.team3.ternaryoperator.domain.comment.repository;

import com.team3.ternaryoperator.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
