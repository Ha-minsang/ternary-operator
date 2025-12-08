package com.team3.ternaryoperator.domain.auth.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.auth.dto.request.LoginRequest;
import com.team3.ternaryoperator.domain.auth.dto.response.LoginResponse;
import com.team3.ternaryoperator.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }
}
