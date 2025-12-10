package com.team3.ternaryoperator.domain.comment.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.comment.model.dto.CommentDto;
import com.team3.ternaryoperator.domain.comment.model.dto.CommentGetDto;
import com.team3.ternaryoperator.domain.comment.model.request.CommentCreateRequest;
import com.team3.ternaryoperator.domain.comment.model.request.CommentUpdateRequest;
import com.team3.ternaryoperator.domain.comment.model.response.CommentGetResponse;
import com.team3.ternaryoperator.domain.comment.model.response.CommentResponse;
import com.team3.ternaryoperator.domain.comment.model.response.CommentUpdateResponse;
import com.team3.ternaryoperator.domain.comment.repository.CommentRepository;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.enums.UserRole;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public CommentResponse createComment(Long taskId, Long userId, CommentCreateRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = new Comment(
                request.getContent(),
                user,
                task,
                parent
        );

        Comment saved = commentRepository.save(comment);

        return CommentResponse.from(CommentDto.from(saved));
    }

    public PageResponse<CommentGetResponse> getComments(Long taskId, String sort, Pageable pageable) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        Sort sortOption = sort.equalsIgnoreCase("oldest")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable finalPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortOption
        );

        Page<Comment> comments = commentRepository.findByTask(task, finalPageable);

        // commentPage.map(comment -> CommentResponse.from(comment)) 과 동일하다
        Page<CommentGetResponse> mapped = comments
                .map(comment -> CommentGetResponse.from(CommentGetDto.from(comment)));

        return PageResponse.from(mapped);
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long taskId, Long commentId, Long userId, CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getTask().getId().equals(taskId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_EXIST);
        }

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN_ONLY_USER);
        }

        comment.update(request.getContent());

        CommentDto dto = CommentDto.from(comment);

        return CommentUpdateResponse.from(dto);
    }

    @Transactional
    public void deleteComment(Long taskId, Long commentId, AuthUser authUser) {

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 작성자 or ADMIN 권한 체크
        if (!comment.getUser().getId().equals(authUser.getId())
                && authUser.getRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.COMMENT_NOT_DELETE_AUTHORIZATION);
        }

        // 대댓글 soft delete
        List<Comment> childComments = commentRepository.findByParentComment_Id(comment.getId());
        System.out.println("자식 댓글들" + childComments);
        childComments.forEach(Comment::softDelete);

        // 부모 soft delete
        comment.softDelete();

    }

}
