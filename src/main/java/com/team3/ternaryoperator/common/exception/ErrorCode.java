package com.team3.ternaryoperator.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Auth(인증,권한) 관련 ErrorCode
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_001", "아이디 또는 비밀번호가 올바르지 않습니다."),
    NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "AUTH_002", "로그인이 되어 있지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_003", "해당 작업을 수행할 권한이 없습니다."),
    ALREADY_LOGGED_IN(HttpStatus.CONFLICT, "AUTH_004", "이미 로그인이 되어 있습니다."),

    // Common 공통 ErrorCode
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON_001", "요청 값이 올바르지 않습니다: ");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
