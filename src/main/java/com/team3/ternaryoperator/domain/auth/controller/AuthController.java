package com.team3.ternaryoperator.domain.auth.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.auth.dto.request.LoginRequest;
import com.team3.ternaryoperator.domain.auth.dto.request.VerifyPasswordRequest;
import com.team3.ternaryoperator.domain.auth.dto.response.LoginResponse;
import com.team3.ternaryoperator.domain.auth.dto.response.VerifyPasswordResponse;
import com.team3.ternaryoperator.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "로그인 성공."));
    }

    @PostMapping("/users/verify-password")
    public ResponseEntity<CommonResponse<VerifyPasswordResponse>> checkPassword(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody VerifyPasswordRequest request
    ) {
        VerifyPasswordResponse response = authService.checkPassword(authUser.getId(), request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "비밀번호가 확인되었습니다."));
    }
}
