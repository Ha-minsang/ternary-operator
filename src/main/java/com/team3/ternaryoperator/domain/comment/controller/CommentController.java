package com.team3.ternaryoperator.domain.comment.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.comment.dto.request.CommentCreateRequest;
import com.team3.ternaryoperator.domain.comment.dto.response.CommentResponse;
import com.team3.ternaryoperator.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

        return ResponseEntity.status(201).body(CommonResponse.success(response));
    }
}
