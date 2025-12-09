package com.team3.ternaryoperator.domain.comment.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.domain.comment.dto.request.CommentCreateRequest;
import com.team3.ternaryoperator.domain.comment.dto.response.CommentResponse;
import com.team3.ternaryoperator.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
            @PathVariable Long taskId,
            @Valid @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        Long userId = authUser.getId();

        CommentResponse response = commentService.createComment(taskId, userId, request);

        return ResponseEntity.ok(CommonResponse.success(response, "댓글이 작성되었습니다."));
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<CommonResponse<PageResponse<CommentResponse>>> getTaskComments(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "newest") String sort,
            @PageableDefault(page = 0, size = 10) Pageable pageable
            ) {
        PageResponse<CommentResponse> response = commentService.getComments(taskId, sort, pageable);

        return ResponseEntity.ok(CommonResponse.success(response, "댓글 목록을 조회했습니다."));
    }
}
