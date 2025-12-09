package com.team3.ternaryoperator.domain.task.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.task.model.request.TaskUpdateRequest;
import com.team3.ternaryoperator.domain.task.model.response.TaskDetailResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskGetResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskResponse;
import com.team3.ternaryoperator.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // 작업 목록 조회(페이징, 필터링)
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<TaskGetResponse>>> getAllTask(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long assigneeId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
            ) {
        PageResponse<TaskGetResponse> response = taskService.getAllTask(status, search, assigneeId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "작업 목록 조회 성공"));
    }

    // 작업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteTask(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id
    ) {
        taskService.deleteTask(authUser, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(null, "작업이 삭제되었습니다."));
    }
}