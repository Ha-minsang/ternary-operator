package com.team3.ternaryoperator.domain.user.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.user.model.request.UserCreateRequest;
import com.team3.ternaryoperator.domain.user.model.response.UserGetResponse;
import com.team3.ternaryoperator.domain.user.model.response.UserResponse;
import com.team3.ternaryoperator.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CommonResponse<UserResponse>> signUp(
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(response, "회원가입이 완료되었습니다."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<UserGetResponse>> getUser(
            @PathVariable Long id
    ) {
        UserGetResponse response = userService.getUser(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(response, "사용자 정보 조회 성공."));
    }

    @GetMapping
    public CommonResponse<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return CommonResponse.success(users, "사용자 목록 조회 성공");
    }
}
