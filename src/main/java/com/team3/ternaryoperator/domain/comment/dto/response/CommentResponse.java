package com.team3.ternaryoperator.domain.comment.dto.response;

import com.team3.ternaryoperator.common.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private final Long id;
    private final Long taskId;
    private final Long userId;
    private final UserInfo user;
    private final String content;
    private final Long parentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class UserInfo {
        private Long id;
        private String username;
        private String name;
    }

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .user(
                        UserInfo.builder()
                                .id(comment.getUser().getId())
                                .username(comment.getUser().getUsername())
                                .name(comment.getUser().getName())
                                .build()
                )
                .content(comment.getContent())
                .parentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getModifiedAt())
                .build();
    }
}
