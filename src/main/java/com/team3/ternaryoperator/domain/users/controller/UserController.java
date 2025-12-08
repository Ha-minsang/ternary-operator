package com.team3.ternaryoperator.domain.users.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.users.dto.request.UserCreateRequest;
import com.team3.ternaryoperator.domain.users.dto.response.UserCreateResponse;
import com.team3.ternaryoperator.domain.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CommonResponse<UserCreateResponse>> signUp(
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserCreateResponse response = userService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(response, "회원가입이 완료되었습니다."));
    }
}
