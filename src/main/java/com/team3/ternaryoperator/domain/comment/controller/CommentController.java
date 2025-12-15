package com.team3.ternaryoperator.domain.comment.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.domain.comment.model.request.CommentCreateRequest;
import com.team3.ternaryoperator.domain.comment.model.request.CommentUpdateRequest;
import com.team3.ternaryoperator.domain.comment.model.response.CommentGetResponse;
import com.team3.ternaryoperator.domain.comment.model.response.CommentResponse;
import com.team3.ternaryoperator.domain.comment.model.response.CommentUpdateResponse;
import com.team3.ternaryoperator.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommonResponse<CommentResponse>> createComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long taskId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        Long userId = authUser.getId();
        CommentResponse response = commentService.createComment(taskId, userId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "댓글이 작성되었습니다."));
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<CommonResponse<PageResponse<CommentGetResponse>>> getTaskComments(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "newest") String sort,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        PageResponse<CommentGetResponse> response = commentService.getComments(taskId, sort, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "댓글 목록을 조회했습니다."));
    }

    @PutMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<CommentUpdateResponse>> updateComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        Long userId = authUser.getId();
        CommentUpdateResponse response = commentService.updateComment(taskId, commentId, userId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "댓글이 수정되었습니다."));
    }

    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<Void>> deleteComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long taskId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(taskId, commentId, authUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(null, "댓글이 삭제되었습니다."));
    }
}
