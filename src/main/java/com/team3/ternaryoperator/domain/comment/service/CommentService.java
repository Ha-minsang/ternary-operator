package com.team3.ternaryoperator.domain.comment.service;

import com.team3.ternaryoperator.common.aspect.ActivityLog;
import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CommentException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.common.exception.TaskException;
import com.team3.ternaryoperator.common.exception.UserException;
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

    // 댓글 생성
    @Transactional
    @ActivityLog
    public CommentResponse createComment(Long taskId, Long userId, CommentCreateRequest request) {
        Task task = getTaskByIdOrThrow(taskId);
        User user = getUserByIdOrThrow(userId);

        Comment parent = getParentCommentIfExists(request.getParentId());

        Comment comment = new Comment(request.getContent(), user, task, parent);
        Comment saved = commentRepository.save(comment);

        activityService.saveActivity(ActivityType.COMMENT_CREATED, userId, taskId, task.getTitle());

        return CommentResponse.from(CommentDto.from(saved));
    }

    // 댓글 조회 (페이지네이션 포함)
    @Transactional(readOnly = true)
    public PageResponse<CommentGetResponse> getComments(Long taskId, String sort, Pageable pageable) {
        Task task = getTaskByIdOrThrow(taskId);

        Pageable parentPageable = getPageableWithSort(pageable, sort);

        Page<Comment> parentPage = commentRepository.findParentCommentsByTask(task, parentPageable);
        List<Long> parentIds = getParentIds(parentPage);

        List<Comment> replies = getRepliesByParentIds(parentIds);
        Map<Long, List<Comment>> repliesMap = groupRepliesByParentId(replies);

        List<CommentGetResponse> flatList = createFlatList(parentPage, repliesMap);
        Page<CommentGetResponse> resultPage = new PageImpl<>(flatList, pageable, parentPage.getTotalElements());

        return PageResponse.from(resultPage);
    }

    // 댓글 수정
    @Transactional
    @ActivityLog
    public CommentUpdateResponse updateComment(Long taskId, Long commentId, Long userId, CommentUpdateRequest request) {
        Comment comment = getCommentByIdOrThrow(commentId, taskId);

        validateCommentOwner(comment, userId);
        comment.update(request.getContent());

        activityService.saveActivity(ActivityType.COMMENT_UPDATED, userId, taskId, comment.getTask().getTitle());

        return CommentUpdateResponse.from(CommentDto.from(comment));
    }

    // 댓글 삭제
    @Transactional
    @ActivityLog
    public void deleteComment(Long taskId, Long commentId, AuthUser authUser) {
        Comment comment = getCommentByIdOrThrow(commentId, taskId);
        validateDeletePermission(comment, authUser);

        activityService.saveActivity(ActivityType.COMMENT_DELETED, comment.getUser().getId(), taskId, comment.getTask().getTitle());

        deleteChildComments(comment);
        comment.softDelete();
    }

    // Task 조회 (없으면 예외 발생)
    private Task getTaskByIdOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(ErrorCode.TASK_NOT_FOUND));
    }

    // User 조회 (없으면 예외 발생)
    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    // 부모 댓글 조회 (있으면 반환, 없으면 null)
    private Comment getParentCommentIfExists(Long parentId) {
        if (parentId != null) {
            return commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
        }
        return null;
    }

    // 페이징된 댓글 조회용 Pageable 설정
    private Pageable getPageableWithSort(Pageable pageable, String sort) {
        Sort sortOption = sort.equalsIgnoreCase("oldest")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOption);
    }

    // 부모 댓글 ID 목록 조회
    private List<Long> getParentIds(Page<Comment> parentPage) {
        return parentPage.getContent().stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }

    // 부모 댓글 ID로 대댓글 조회
    private List<Comment> getRepliesByParentIds(List<Long> parentIds) {
        return parentIds.isEmpty() ? new ArrayList<>() : commentRepository.findRepliesByParentIds(parentIds);
    }

    // 대댓글을 부모 ID별로 그룹화
    private Map<Long, List<Comment>> groupRepliesByParentId(List<Comment> replies) {
        return replies.stream()
                .collect(Collectors.groupingBy(comment -> comment.getParentComment().getId()));
    }

    // 댓글 리스트 정렬
    private List<CommentGetResponse> createFlatList(Page<Comment> parentPage, Map<Long, List<Comment>> repliesMap) {
        List<CommentGetResponse> flatList = new ArrayList<>();
        for (Comment parent : parentPage.getContent()) {
            flatList.add(CommentGetResponse.from(CommentGetDto.from(parent)));

            List<Comment> children = repliesMap.getOrDefault(parent.getId(), List.of());
            for (Comment child : children) {
                flatList.add(CommentGetResponse.from(CommentGetDto.from(child)));
            }
        }
        return flatList;
    }

    // TaskId로 댓글 조회 (없으면 예외 발생)
    private Comment getCommentByIdOrThrow(Long commentId, Long taskId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getTask().getId().equals(taskId)) {
            throw new CommentException(ErrorCode.COMMENT_NOT_EXIST);
        }
        return comment;
    }

    // 댓글 작성자 확인 (권한 검사)
    private void validateCommentOwner(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentException(ErrorCode.COMMENT_FORBIDDEN_NOT_OWNER);
        }
    }

    // 댓글 삭제 권한 확인 (작성자 또는 관리자)
    private void validateDeletePermission(Comment comment, AuthUser authUser) {
        if (!comment.getUser().getId().equals(authUser.getId()) && authUser.getRole() != UserRole.ADMIN) {
            throw new CommentException(ErrorCode.COMMENT_DELETE_PERMISSION_DENIED);
        }
    }

    // 대댓글 삭제
    private void deleteChildComments(Comment comment) {
        List<Comment> childComments = commentRepository.findByParentComment_Id(comment.getId());
        childComments.forEach(Comment::softDelete);
    }
}
