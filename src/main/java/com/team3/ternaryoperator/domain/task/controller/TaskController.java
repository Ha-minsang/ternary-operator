package com.team3.ternaryoperator.domain.task.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.task.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.task.model.request.TaskUpdateRequest;
import com.team3.ternaryoperator.domain.task.model.response.TaskDetailResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskResponse;
import com.team3.ternaryoperator.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // 작업 생성
    @PostMapping
    public ResponseEntity<CommonResponse<TaskResponse>> createTask(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody TaskCreateRequest request
    ) {
        TaskResponse response =  taskService.createTask(authUser.getId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(response, "작업이 생성되었습니다."));
    }

    // 작업 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<TaskResponse>> updateTask(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request
    ) {
        TaskResponse response = taskService.updateTask(authUser.getId(), id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "작업이 수정되었습니다."));
    }

    // 작업 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TaskDetailResponse>> getOneTask(
            @PathVariable Long id
    ) {
        TaskDetailResponse response = taskService.getOneTask(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "작업 조회 성공"));
    }

}