package com.team3.ternaryoperator.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Valid 검증 ErrorCode
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다: "),

    // Auth(인증,권한) 관련 ErrorCode
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),

    // User 관련 ErrorCode
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    USER_DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다."),
    USER_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // Task 관련 ErrorCode
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "작업을 찾을 수 없습니다."),
    TASK_FORBIDDEN_NOT_ASSIGNEE(HttpStatus.FORBIDDEN, "수정 권한이 없습니다"),
    TASK_INVALID_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 상태 값입니다."),

    // Team 관련 ErrorCode
    TEAM_DUPLICATE_NAME(HttpStatus.CONFLICT, "이미 존재하는 팀 이름입니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀 또는 멤버를 찾을 수 없습니다."),
    TEAM_UPDATE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    TEAM_DELETE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),
    TEAM_MEMBER_DELETE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "제거 권한이 없습니다."),
    TEAM_MEMBER_EXIST(HttpStatus.CONFLICT, "팀에 멤버가 존재하여 삭제할 수 없습니다."),
    TEAM_ALREADY_MEMBER(HttpStatus.CONFLICT, "이미 팀에 속한 멤버입니다."),

    // Comment 관련 ErrorCode
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_FORBIDDEN_NOT_OWNER(HttpStatus.FORBIDDEN, "댓글을 수정할 권한이 없습니다."),
    COMMENT_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 작업에 댓글이 존재하지 않습니다."),
    COMMENT_DELETE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "댓글을 삭제할 권한이 없습니다."),

    // Search 관련 ErrorCode
    SEARCH_QUERY_EMPTY(HttpStatus.BAD_REQUEST, "검색어를 입력해주세요.");

    private final HttpStatus status;
    private final String message;
}
