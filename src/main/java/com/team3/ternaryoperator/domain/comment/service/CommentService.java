package com.team3.ternaryoperator.domain.comment.service;

import com.team3.ternaryoperator.common.aspect.ActivityLog;
import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import com.team3.ternaryoperator.domain.activity.service.ActivityService;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ActivityService activityService;

    @Transactional
    @ActivityLog
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

        activityService.saveActivity(ActivityType.COMMENT_CREATED, userId, taskId, task.getTitle());

        return CommentResponse.from(CommentDto.from(saved));
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentGetResponse> getComments(Long taskId, String sort, Pageable pageable) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        // 댓글 정렬만 적용
        Sort sortOption = sort.equalsIgnoreCase("oldest")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable parentPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortOption
        );

        Page<Comment> parentPage =
                commentRepository.findParentCommentsByTask(task, parentPageable);

        List<Long> parentIds = parentPage.getContent().stream()
                .map(Comment::getId)
                .toList();

        // 자식이 없는 경우: 그대로 반환
        if (parentIds.isEmpty()) {
            return PageResponse.from(parentPage.map(c ->
                    CommentGetResponse.from(CommentGetDto.from(c))));
        }

        // 페이징된 parent들의 자식 대댓글 전체 조회
        List<Comment> replies =
                commentRepository.findRepliesByParentIds(parentIds);

        // parentId별 그룹핑
        Map<Long, List<Comment>> repliesMap = replies.stream()
                .collect(Collectors.groupingBy(comment -> comment.getParentComment().getId()));

        List<CommentGetResponse> flatList = new ArrayList<>();

        for (Comment parent : parentPage.getContent()) {

            // 부모 댓글 추가
            flatList.add(CommentGetResponse.from(CommentGetDto.from(parent)));

            // 대댓글 최신순 정렬 추가
            List<Comment> children = repliesMap.getOrDefault(parent.getId(), List.of());

            for (Comment child : children) {
                flatList.add(CommentGetResponse.from(CommentGetDto.from(child)));
            }
        }

        Page<CommentGetResponse> resultPage = new PageImpl<>(
                flatList,
                pageable,
                parentPage.getTotalElements() // 부모 댓글 개수가 전체 개수
        );

        return PageResponse.from(resultPage);
    }

    @Transactional
    @ActivityLog
    public CommentUpdateResponse updateComment(Long taskId, Long commentId, Long userId, CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getTask().getId().equals(taskId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_EXIST);
        }

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN_NOT_OWNER);
        }

        comment.update(request.getContent());

        CommentDto dto = CommentDto.from(comment);

        activityService.saveActivity(ActivityType.COMMENT_UPDATED, userId, taskId, comment.getTask().getTitle());

        return CommentUpdateResponse.from(dto);
    }

    @Transactional
    @ActivityLog
    public void deleteComment(Long taskId, Long commentId, AuthUser authUser) {

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 작성자 or ADMIN 권한 체크
        if (!comment.getUser().getId().equals(authUser.getId())
                && authUser.getRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.COMMENT_DELETE_PERMISSION_DENIED);
        }

        activityService.saveActivity(ActivityType.COMMENT_DELETED, comment.getUser().getId(), taskId, comment.getTask().getTitle());

        // 대댓글 soft delete
        List<Comment> childComments = commentRepository.findByParentComment_Id(comment.getId());
        childComments.forEach(Comment::softDelete);

        // 부모 soft delete
        comment.softDelete();
    }

}
