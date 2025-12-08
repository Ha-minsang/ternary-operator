package com.team3.ternaryoperator.domain.comment.service;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.comment.dto.request.CommentCreateRequest;
import com.team3.ternaryoperator.domain.comment.dto.response.CommentResponse;
import com.team3.ternaryoperator.domain.comment.repository.CommentRepository;
import com.team3.ternaryoperator.domain.tasks.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public CommentResponse createComment(Long taskId, Long userId, CommentCreateRequest request) {

        // 1. Task 존재 확인
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        // 2. User 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 3. 부모 댓글 있는 경우 검증
        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        }

        // 4. 댓글 생성
        Comment comment = new Comment(
                request.getContent(),
                user,
                task,
                parent
        );

        Comment saved = commentRepository.save(comment);

        return CommentResponse.from(saved);
    }
}
