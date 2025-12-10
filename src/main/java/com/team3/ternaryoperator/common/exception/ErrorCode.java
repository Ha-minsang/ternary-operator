package com.team3.ternaryoperator.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Auth(인증,권한) 관련 ErrorCode
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다."),
    NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인이 되어 있지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),
    ALREADY_LOGGED_IN(HttpStatus.CONFLICT, "이미 로그인이 되어 있습니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),

    // Common 공통 ErrorCode
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다: "),

    // User 관련 ErrorCode
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "유저가 존재하지 않습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),

    // Task 관련 ErrorCode
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "작업을 찾을 수 없습니다."),
    TASK_FORBIDDEN_ONLY_ASSIGNEE(HttpStatus.FORBIDDEN, "수정 권한이 없습니다"),

    // Team 관련 ErrorCode
    TEAM_NAME_DUPLICATED(HttpStatus.UNAUTHORIZED, "이미 존재하는 팀 이름입니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    NO_PERMISSION_TEAM_UPDATE(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    NO_PERMISSION_TEAM_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),
    EXIST_MEMBER(HttpStatus.CONFLICT, "팀에 멤버가 존재하여 삭제할 수 없습니다."),

    // Comment 관련 ErrorCode
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_FORBIDDEN_ONLY_USER(HttpStatus.FORBIDDEN, "댓글을 수정할 권한이 없습니다."),
    COMMENT_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 작업에 댓글이 존재하지 않습니다."),
    COMMENT_NOT_DELETE_AUTHORIZATION(HttpStatus.FORBIDDEN, "댓글을 삭제할 권한이 없습니다.");

    // Activity 관련 ErrorCode


    private final HttpStatus status;
    private final String message;
}
