package com.team3.ternaryoperator.domain.activity.enums;

import lombok.Getter;

@Getter
public enum ActivityType {

    TASK_CREATED("작업 생성", "새 작업 '%s'를 생성했습니다."),
    TASK_UPDATED("작업 수정", "작업 '%s'를 수정했습니다."),
    TASK_DELETED("작업 삭제", "작업 '%s'를 삭제했습니다."),
    TASK_STATUS_CHANGED("작업 상태 변경", "작업 '%s'의 상태를 변경했습니다."),
    COMMENT_CREATED("댓글 생성", "작업 '%s'에 댓글을 작성했습니다."),
    COMMENT_UPDATED("댓글 수정", "작업 '%s'의 댓글을 수정했습니다."),
    COMMENT_DELETED("댓글 삭제", "작업 '%s'의 댓글을 삭제했습니다.");

    private final String action;
    private final String description;

    ActivityType(String action, String description) {
        this.action = action;
        this.description = description;
    }

    public String createDescription(String taskTitle) {
        return String.format(description, taskTitle);
    }
}
