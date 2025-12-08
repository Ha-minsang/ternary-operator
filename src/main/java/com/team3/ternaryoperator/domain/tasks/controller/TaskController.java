package com.team3.ternaryoperator.domain.tasks.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.tasks.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.tasks.model.response.TaskCreateResponse;
import com.team3.ternaryoperator.domain.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CommonResponse<TaskCreateResponse>> createTask(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody TaskCreateRequest request
    ) {
        TaskCreateResponse response =  taskService.createTask(authUser.getId(), request);;
        return ResponseEntity.ok(CommonResponse.success(response, "작업이 생성되었습니다."));
    }



}