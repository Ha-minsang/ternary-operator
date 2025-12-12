package com.team3.ternaryoperator.domain.auth.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.auth.dto.request.LoginRequest;
import com.team3.ternaryoperator.domain.auth.dto.request.VerifyPasswordRequest;
import com.team3.ternaryoperator.domain.auth.dto.response.LoginResponse;
import com.team3.ternaryoperator.domain.auth.dto.response.VerifyPasswordResponse;
import com.team3.ternaryoperator.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);

        return ResponseEntity
                .ok(CommonResponse
                        .success(response, "로그인 성공."));
    }

    @PostMapping("/verify-password")
    public ResponseEntity<CommonResponse<VerifyPasswordResponse>> checkPassword(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody VerifyPasswordRequest request
    ) {
        VerifyPasswordResponse response = authService.checkPassword(authUser.getId(), request);

        return ResponseEntity
                .ok(CommonResponse
                        .success(response, "비밀번호가 확인되었습니다."));
    }
}
