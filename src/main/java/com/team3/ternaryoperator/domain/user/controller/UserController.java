package com.team3.ternaryoperator.domain.user.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.user.model.request.UserCreateRequest;
import com.team3.ternaryoperator.domain.user.model.request.UserUpdateRequest;
import com.team3.ternaryoperator.domain.user.model.response.UserDetailResponse;
import com.team3.ternaryoperator.domain.user.model.response.UserResponse;
import com.team3.ternaryoperator.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<CommonResponse<UserDetailResponse>> getUser(
            @PathVariable Long id
    ) {
        UserDetailResponse response = userService.getUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "사용자 정보 조회 성공."));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(users, "사용자 목록 조회 성공"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<UserDetailResponse>> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        UserDetailResponse response = userService.updateUser(authUser, id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "사용자 정보가 수정되었습니다."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse<Void>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id
    ) {
        userService.deleteUser(authUser, id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(null, "회원 탈퇴가 완료되었습니다."));
    }

    @GetMapping("/available")
    public ResponseEntity<CommonResponse<List<UserResponse>>> getAvailableUsers(
            @RequestParam(required = false) Long teamId
    ) {
        List<UserResponse> response = userService.getAvailableUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(response, "추가 가능한 사용자 목록 조회 성공"));
    }
}
