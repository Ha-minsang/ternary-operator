package com.team3.ternaryoperator.domain.comment.dto.response;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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
    @AllArgsConstructor
    public static class UserInfo {
        private final Long id;
        private final String username;
        private final String name;

        public static UserInfo from(User user) {
            return new UserInfo(
                    user.getId(),
                    user.getUsername(),
                    user.getName()
            );
        }
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                UserInfo.from(comment.getUser()),
                comment.getContent(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
